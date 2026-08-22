# 🏥 Sistema de Gerenciamento e Triagem de Pacientes (Protocolo de Manchester)

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](#)
[![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen.svg)](#)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> Sistema de triagem hospitalar e reclassificação dinâmica baseado no **Protocolo de Manchester**. O algoritmo lê dados de pacientes em espera, aplica regras de escalonamento por tempo excedido, ordena os atendimentos por prioridade clínica/preferencial e exporta a fila final.

---

## 📌 Sumário
- [Visão Geral](#-visão-geral)
- [Regras de Negócio](#-regras-de-negócio)
  - [1. Escalonamento Dinâmico por Tempo](#1-escalonamento-dinâmico-por-tempo)
  - [2. Critérios de Desempate e Ordenação](#2-critérios-de-desempate-e-ordenação)
- [Estrutura dos Arquivos](#-estrutura-dos-arquivos)
- [Como Executar](#-como-executar)
- [Exemplo de Entrada e Saída](#-exemplo-de-entrada-e-saída)
- [Estrutura do Código](#-estrutura-do-código)
- [Autor](#-autor)

---

## 📖 Visão Geral

O programa processa uma lista de pacientes a partir de um arquivo CSV (`pacientes.csv`), onde cada registro contém:
1. **Prioridade Inicial (Cor de Triagem)**: `Vermelho`, `Laranja`, `Amarelo`, `Verde` ou `Azul`.
2. **Categoria Preferencial**: Categoria prioritária (ex: Idoso, Gestante, PCD) ou `N/A`.
3. **Tempo de Espera**: Tempo decorrido (em minutos).

Após a leitura, o sistema:
- Reclassifica pacientes cujo tempo de espera ultrapassou os limites clínicos toleráveis.
- Ordena a lista com base em um comparador multinível de prioridades.
- Gera o arquivo `OrdemDeAtendimentos.csv` pronto para a equipe de atendimento.

---

## ⚖️ Regras de Negócio

### 1. Escalonamento Dinâmico por Tempo
Pacientes com espera excessiva são automaticamente promovidos para o nível de urgência superior:

| Prioridade Original | Tempo de Espera | Nova Prioridade |
| :--- | :--- | :--- |
| 🟠 **Laranja** | $> 10\text{ min}$ | 🔴 **Vermelho** (Emergência máxima) |
| 🟡 **Amarelo** | $> 60\text{ min}$ | 🟠 **Laranja** (Muito urgente) |
| 🟢 **Verde** | $> 120\text{ min}$ | 🟡 **Amarelo** (Urgente) |
| 🔵 **Azul** | $> 240\text{ min}$ | 🟢 **Verde** (Pouco urgente) |

---

### 2. Critérios de Desempate e Ordenação

A fila é ordenada pelo `comparadorPrioridade` segundo a seguinte hierarquia de decisão:

```mermaid
flowchart TD
    A[Comparar Nível de Prioridade] -->|Cores Diferentes| B[Prioridade mais alta vai primeiro]
    A -->|Mesma Cor| C{É Vermelho ou Laranja?}
    C -->|Sim| D[Maior Tempo de Espera vai primeiro]
    C -->|Não| E{Mesma Categoria Preferencial?}
    E -->|Não| F[Categoria Preferencial > N/A]
    E -->|Sim| G[Maior Tempo de Espera vai primeiro]
