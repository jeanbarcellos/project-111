# **Plano de Capacidade (Apenas Tabela `url`)**

---

## **1. Tamanho estimado por linha (row) da tabela `url`**

### **Tabela `url`**

| Coluna       | Tipo          | Tamanho estimado (bytes)        |
| ------------ | ------------- | ------------------------------- |
| `hash`       | VARCHAR(6)    | 6                               |
| `target_url` | VARCHAR(2083) | 2083 (tamanho médio de uma URL) |
| `created_at` | TIMESTAMP(6)  | 8                               |
| `expires_at` | DATE          | 4                               |
| `user_id`    | UUID          | 16                              |

- **Total sem índices**:
  6 + 2083 + 8 + 4 + 16 = 2117 bytes

- **Índices**:

  - Índice primário em `hash`: 6 bytes
  - Índice estrangeiro em `user_id`: 16 bytes

- **Tamanho total por linha**:
  2117 + 6 + 16 = **2139 bytes** ou **2,139 Bb**

---

## **2. Planejamento de Capacidade (Tabela `url`)**

### **Cenário 1:**

- **10 URLs/minuto**
- URLs criadas por dia:
  10 URLs \* 60 minutos \* 24 horas = **14.400 URLs/Dia**

- **Armazenamento diário**:
  14.400 \* 2139 butes = 30.801.600 bytes (~30.801 KB ou **~30.8 MB**)

- **Armazenamento anual**:
  30.8 \* 365 = 11.2 GB

- **Armazenamento em 5 anos**:
  11.2 \* 5 = 56 GB

---

### **Cenário 2:**

- **100 URLs/minuto**
- URLs criadas por dia:
  100 \* 60 \* 24 = 144,000

- **Armazenamento diário**:
  144,000 \* 2139 = 307,200,000 bytes (~307.2 MB)

- **Armazenamento anual**:
  307.2 \* 365 = 112 GB

- **Armazenamento em 5 anos**:
  112 \* 5 = 560 GB

---

### **Cenário 3:**

- **100 URLs/minuto**
- **Leitura/Escrita Ratio**: 500:1
- URLs criadas por dia:
  100 \* 60 \* 24 = 144,000

- **Armazenamento diário**:
  144,000 \* 2139 = 307,200,000 bytes (~307.2 MB)

- **Armazenamento anual**:
  307.2 \* 365 = 112 GB

- **Armazenamento em 5 anos**:
  112 \* 5 = 560 GB

---

### **Cenário 4:**

- **1000 URLs/minuto**
- **Leitura/Escrita Ratio**: 500:1
- URLs criadas por dia:
  1000 \* 60 \* 24 = 1,440,000

- **Armazenamento diário**:
  1,440,000 \* 2139 = 3,072,000,000 bytes (~3.1 GB)

- **Armazenamento anual**:
  3.1 \* 365 = 1.12 TB

- **Armazenamento em 5 anos**:
  1.12 \* 5 = 5.6 TB

---

## **2.1 Cache**

- **80% das URLs serão utilizadas**
- TTL do cache: **24 horas**
- Tamanho estimado por URL no cache: 2089 bytes

### **Cenário 1:**

- URLs no cache por dia:
  14,400 \* 0.8 = 11,520

- **Armazenamento no cache (24h)**:
  11,520 \* 2089 = 24,046,080 bytes (~24 MB)

---

### **Cenário 2:**

- URLs no cache por dia:
  144,000 \* 0.8 = 115,200

- **Armazenamento no cache (24h)**:
  115,200 \* 2089 = 240,460,800 bytes (~240 MB)

---

### **Cenário 3:**

- URLs no cache por dia:
  144,000 \* 0.8 = 115,200

- **Armazenamento no cache (24h)**:
  115,200 \* 2089 = 240,460,800 bytes (~240 MB)

---

### **Cenário 4:**

- URLs no cache por dia:
  1,440,000 \* 0.8 = 1,152,000

- **Armazenamento no cache (24h)**:
  1,152,000 \* 2089 = 2,404,608,000 bytes (~2.4 GB)

---

## **2.2 Largura de Banda (Bandwidth)**

### **Cálculo**

- Cada leitura envolve: 2089 bytes (hash + target URL)
- Cada escrita envolve: 2139 bytes (linha completa)

---

### **Cenário 1:**

- Leituras por dia:
  14,400 \* 50 = 720,000

- **Leituras (diárias)**:
  720,000 \* 2089 = 1,504,080,000 bytes (~1.5 GB)

- **Escritas (diárias)**:
  14,400 \* 2139 = 30,801,600 bytes (~30.8 MB)

- **Total diário**:
  1.5 \* 1024 = 1.53 GB

---

### **Cenário 2:**

- Leituras por dia:
  144,000 \* 50 = 7,200,000

- **Leituras (diárias)**:
  7,200,000 \* 2089 = 15,040,800,000 bytes (~15 GB)

- **Escritas (diárias)**:
  144,000 \* 2139 = 307,200,000 bytes (~307.2 MB)

- **Total diário**:
  15.3 GB

---

### **Cenário 3:**

- Leituras por dia:
  144,000 \* 500 = 72,000,000

- **Leituras (diárias)**:
  72,000,000 \* 2089 = 150,408,000,000 bytes (~150.4 GB)

- **Escritas (diárias)**:
  144,000 \* 2139 = 307,200,000 bytes (~307.2 MB)

- **Total diário**:
  150.7 GB

---

### **Cenário 4:**

- Leituras por dia:
  1,440,000 \* 500 = 720,000,000

- **Leituras (diárias)**:
  720,000,000 \* 2089 = 1,504,080,000,000 bytes (~1.5 TB)

- **Escritas (diárias)**:
  1,440,000 \* 2139 = 3,072,000,000 bytes (~3.1 GB)

- **Total diário**:
  1.503 TB

## **Resumo Final do Plano de Capacidade**

| **Cenário** | **URLs/minuto** | **Leitura:Escrita** | **Armazenamento Diário** | **Armazenamento Anual** | **Armazenamento 5 Anos** | **Cache Diário** | **Largura de Banda (Diário)** |
| ----------- | --------------- | ------------------- | ------------------------ | ----------------------- | ------------------------ | ---------------- | ----------------------------- |
| **1**       | 10              | 50:1                | 30.8 MB                  | 11.2 GB                 | 56 GB                    | ~24 MB           | ~1.53 GB                      |
| **2**       | 100             | 50:1                | 307.2 MB                 | 112 GB                  | 560 GB                   | ~240 MB          | ~15.3 GB                      |
| **3**       | 100             | 500:1               | 307.2 MB                 | 112 GB                  | 560 GB                   | ~240 MB          | ~150.7 GB                     |
| **4**       | 1000            | 500:1               | 3.1 GB                   | 1.12 TB                 | 5.6 TB                   | ~2.4 GB          | ~1.5 TB                       |

---

## **Legenda**

- **URLs/minuto**: Taxa de novas URLs geradas por minuto.
- **Leitura:Escrita**: Proporção de leituras e escritas na aplicação.
- **Armazenamento Diário**: Quantidade de espaço necessário para armazenar os dados em um dia.
- **Armazenamento Anual**: Espaço estimado para armazenar os dados durante 1 ano.
- **Armazenamento 5 Anos**: Espaço estimado para armazenar os dados durante 5 anos.
- **Cache Diário**: Espaço necessário no cache para armazenar 80% das URLs usadas diariamente.
- **Largura de Banda (Diário)**: Quantidade de dados trafegados por dia entre leitura e escrita.

## Anexo

- [Planilha para calculo](calc.ods)