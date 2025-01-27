# **Plano de Capacidade (Apenas Tabela `url`)**

## Cenários de evolução

- **Cenário 1**
  - **10** novas URLs por minuto
  - Ratio de leitura e escrita de **50:1**
  - Tráfego em **500** RPM
- **Cenário 2**
  - **100** novas URLs por minuto (RPM)
  - Ratio de leitura e escrita de **50:1**
  - Tráfego em **5.000** RPM
- **Cenário 3**
  - **100** novas URLs por minuto
  - Ratio de leitura e escrita de **500:1**
  - Tráfego em **50.000** RPM
- **Cenário 4**
  - **1000** novas URLs por minuto (RPM)
  - Ratio de leitura e escrita de **500:1**
  - Tráfego em **500.000** RPM

---

## **1) Tamanho estimado por linha (row) da tabela `url`**

### **Tabela `url`**

| Coluna       | Tipo          | Tamanho estimado (bytes)        |
| ------------ | ------------- | ------------------------------- |
| `hash`       | VARCHAR(6)    | 6                               |
| `target_url` | VARCHAR(2083) | 2048 (tamanho médio de uma URL) |
| `created_at` | TIMESTAMP(6)  | 8                               |
| `expires_at` | DATE          | 4                               |
| `user_id`    | UUID          | 16                              |

- **Total sem índices**:
  6 + 2048 + 8 + 4 + 16 = 2082 bytes

- **Índices**:

  - Índice primário em `hash`: 6 bytes
  - Índice estrangeiro em `user_id`: 16 bytes

- **Tamanho total por linha**:
  2082 + 6 + 16 = **2104 bytes** ou **2,104 Bb**

---

## **2) Espaço necessário para armazenamento (Tabela `url`)**

### **Premissas**

1. **Escrita por minuto**:

   - Cenário 1: 10 novas URLs/minuto
   - Cenário 2: 100 novas URLs/minuto
   - Cenário 3: 100 novas URLs/minuto
   - Cenário 4: 1000 novas URLs/minuto

2. **Cálculos de armazenamento diário, anual e em 5 anos**:
   - **Tamanho de escrita por linha**:
     2.104 bytes.
   - **Linhas escritas por dia**:
     Escritas/minuto \* 1.440 minutos/dia.
   - **Armazenamento anual**:
     Escritas/dia \* 365.
   - **Armazenamento em 5 anos**:
     Escritas/ano \* 5.

### **Cenário 1:**

- **10 URLs/minuto**

- **URLs criadas por dia**:
  10 URLs \* 60 minutos \* 24 horas = **14.400 URLs/Dia**

- **Escritas por dia**:
  10 \* 1440 = 14,400 linhas/dia
  14,400 \* 2104 = 30,297,600 bytes/dia ~ **28.9 MB/dia**.

- **Armazenamento anual**:
  30,297,600 \* 365 = 11,049,600,000 bytes/ano ~ **10.3 GB/ano**.

- **Armazenamento em 5 anos**:
  11,049,600,000 \* 5 = 55,248,000,000 bytes ~ **51.7 GB/5 anos**.

---

### **Cenário 2:**

- **100 URLs/minuto**

- **URLs criadas por dia**:
  100 \* 60 \* 24 = **144,000 URLs/Dia**

- **Escritas por dia**:
  100 \* 1440 = 144,000 linhas/dia
  144,000 \* 2104 = 302,976,000 bytes/dia ~ **289 MB/dia**.

- **Armazenamento anual**:
  302,976,000 \* 365 = 110,496,000,000 bytes/ano ~ **102.9 GB/ano**.
- **Armazenamento em 5 anos**:
  110,496,000,000 \* 5 = 552,480,000,000 bytes ~ **514.7 GB/5 anos**.

---

### **Cenário 3:**

Mesmo número de escritas por minuto que o **Cenário 2**, então os valores serão os mesmos:

- **289 MB/dia**, **102.9 GB/ano**, **514.7 GB/5 anos**.

---

### **Cenário 4:**

- **1000 URLs/minuto**

- **Leitura/Escrita Ratio**: 500:1

- URLs criadas por dia:
  1000 \* 60 \* 24 = **1,440,000 URLs/Dia**

- **Escritas por dia**:
  1000 \* 1440 = 1,440,000 linhas/dia
  1,440,000 \* 2104 = 3,029,760,000 bytes/dia ~ **2.82 GB/dia**.

- **Armazenamento anual**:
  3,029,760,000 \* 365 = 1,104,960,000,000 bytes/ano ~ **1.03 TB/ano**.

- **Armazenamento em 5 anos**:
  1,104,960,000,000 \* 5 = 5,524,800,000,000 bytes ~ **5.17 TB/5 anos**.

---

## **3) Armazenamento em cache**

**Premissas**:

- 80% das URLs lidas serão armazenadas em cache.
- TTL do cache: 24 horas.
- Cada leitura: 2089 bytes.

### Cálculo para cada cenário:

- **Leituras por minuto**: Tráfego RPM \* 80\%.
- **Cache diário**:
  Leituras por minuto \* 2089 bytes \* 1440.

| Cenário | Leituras RPM | Leituras no Cache RPM | Cache Diário (GB) |
| ------- | ------------ | --------------------- | ----------------- |
| 1       | 500          | 400                   | 1.1               |
| 2       | 5000         | 4000                  | 11.2              |
| 3       | 50000        | 40000                 | 120.1             |
| 4       | 500000       | 400000                | 1120.6            |

## **4) Largura de banda (Bandwidth)**

## Anexo

- [Planilha para calculo](calc.ods)
