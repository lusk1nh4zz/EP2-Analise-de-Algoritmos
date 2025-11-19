import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

public class gerenciamento_de_pacientes {
    public class Paciente {
        private String prioridade;
        private String categoria;
        private int tempoEspera;

        public Paciente(String prioridade, String categoria, int tempoEspera) {
            this.prioridade = prioridade;
            this.categoria = categoria;
            this.tempoEspera = tempoEspera;
            aplicarEscalonamento();
        }

        public String getPrioridade() {
            return prioridade;
        }

        public String getCategoria() {
            return categoria;
        }

        public int getTempoEspera() {
            return tempoEspera;
        }

        private void aplicarEscalonamento() {
            if (this.prioridade.equalsIgnoreCase("Laranja") && this.tempoEspera > 10) {
                this.prioridade = "Vermelho";
            } else if (this.prioridade.equalsIgnoreCase("Amarelo") && this.tempoEspera > 60) {
                this.prioridade = "Laranja";
            } else if (this.prioridade.equalsIgnoreCase("Verde") && this.tempoEspera > 120) {
                this.prioridade = "Amarelo";
            } else if (this.prioridade.equalsIgnoreCase("Azul") && this.tempoEspera > 240) {
                this.prioridade = "Verde";
            }
        }
    }

    public class comparadorPrioridade implements java.util.Comparator<Paciente> {
        @Override
        public int compare(Paciente p1, Paciente p2) {
            int v1 = obterValorPrioridade(p1.getPrioridade());
            int v2 = obterValorPrioridade(p2.getPrioridade());

            if (v1 != v2) {
                return v2 - v1;
            }

            if (v1 == 4 || v1 == 3) {
                return p2.getTempoEspera() - p1.getTempoEspera();
            }

            int c1 = obterValorCategoria(p1.getCategoria());
            int c2 = obterValorCategoria(p2.getCategoria());

            if (c1 != c2) {
                return c2 - c1;
            }

            return p2.getTempoEspera() - p1.getTempoEspera();
        }

        private int obterValorPrioridade(String prioridade) {
            switch (prioridade.toLowerCase()) {
                case "vermelho":
                    return 4;
                case "laranja":
                    return 3;
                case "amarelo":
                    return 2;
                case "verde":
                    return 1;
                case "azul":
                    return 0;
                default:
                    return -1;
            }
        }
        private int obterValorCategoria(String categoria) {
            if (categoria.equalsIgnoreCase("N/A")) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("pacientes.csv"));
        List<Paciente> pacientes = new java.util.ArrayList<>();
        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            String[] divisao = linha.split(",");
            String prioridade = divisao[0].trim();
            String categoria = divisao[1].trim();
            int tempoEspera = Integer.parseInt(divisao[2].trim());
            pacientes.add(new gerenciamento_de_pacientes().new Paciente(prioridade, categoria, tempoEspera));
        }
        
        Collections.sort(pacientes, new gerenciamento_de_pacientes().new comparadorPrioridade());

try (PrintWriter writer = new PrintWriter(new FileWriter("OrdemDeAtendimentos.csv"))) {
    for (Paciente p : pacientes) {
        writer.printf("%s, %s, %d\n", 
            p.getPrioridade(), 
            p.getCategoria(), 
            p.getTempoEspera());
    }
    System.out.println("\nArquivo 'OrdemDeAtendimentos.csv' gerado com sucesso.");
    scanner.close();
} catch (IOException e) {
    System.err.println("Erro ao escrever o arquivo: " + e.getMessage());
}
    }
}