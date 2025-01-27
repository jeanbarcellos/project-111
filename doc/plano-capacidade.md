# **Plano de Capacidade**

## Cenários de evolução

- **Cenário 1**
  - **10** novas URLs por minuto (RPM)
  - Ratio de leitura e escrita de **50:1**
- **Cenário 2**
  - **100** novas URLs por minuto (RPM)
  - Ratio de leitura e escrita de **50:1**
- **Cenário 3**
  - **100** novas URLs por minuto (RPM)
  - Ratio de leitura e escrita de **500:1**
- **Cenário 4**
  - **1000** novas URLs por minuto (RPM)
  - Ratio de leitura e escrita de **500:1**

---

## **1. Tamanho estimado por linha (row)**

Estimativo do tamanho de colunas

### **Tabela `user`**

| Coluna       | Tipo         | Tamanho estimado (bytes)          |
| ------------ | ------------ | --------------------------------- |
| `id`         | UUID         | 16                                |
| `name`       | VARCHAR(255) | 255 (tamanho máximo permitido)    |
| `email`      | VARCHAR(255) | 255 (considerando e-mails médios) |
| `password`   | VARCHAR(255) | 255 (assumindo hash armazenado)   |
| `created_at` | TIMESTAMP(0) | 8                                 |

- **Total sem índices**:

  - 16 + 255 + 255 + 255 + 8 = **789 bytes**

- **Índices**:

  - Índice único em `email`: 255 bytes
  - Índice primário em `id`: 16 bytes

- **Tamanho total por linha**:
  - 789 + 255 + 16 = **1.060 bytes**

---

### **Tabela `url`**

| Coluna       | Tipo          | Tamanho estimado (bytes)        |
| ------------ | ------------- | ------------------------------- |
| `hash`       | VARCHAR(6)    | 6                               |
| `target_url` | VARCHAR(2083) | 2083 (tamanho médio de uma URL) |
| `created_at` | TIMESTAMP(6)  | 8                               |
| `expires_at` | DATE          | 4                               |
| `user_id`    | UUID          | 16                              |

- **Total sem índices**:

  - 6 + 2083 + 8 + 4 + 16 = **2.117 bytes**

- **Índices**:

  - Índice primário em `hash`: 6 bytes
  - Índice estrangeiro em `user_id`: 16 bytes

- **Tamanho total por linha**:
  - 2117 + 6 + 16 = **2139 bytes**
    - **2,139 kb**

---

## **2. Planejamento de Capacidade**

### **Tamanho por URL criada**

Cada URL associada a um usuário ocupa: 1060 + 2139 = **3199 bytes**

### **Cenários e Cálculos**

**Cenário 1**:

- **10 URLs/minuto**
- **Leitura/Escrita Ratio**: 50:1
  - URLs criadas por dia:
    10 URLs \* 60 min \* 24 h = **14,400**
  - Leituras por dia:
    14,400 \* 50 = **720,000**
- **Armazenamento diário**:
  14,400 \* 3199 = **46,065,600 bytes (~46 MB)**
- **Armazenamento anual**:
  46 MB \* 365 = **16.8 GB**

---

**Cenário 2**:

- **100 URLs/minuto**
- **Leitura/Escrita Ratio**: 50:1
  - URLs criadas por dia:
    100 \* 60 \* 24 = 144,000
  - Leituras por dia:
    144,000 \* 50 = 7,200,000
- **Armazenamento diário**:
  144,000 \* 3199 = **460,656,000 bytes (~460 MB)**
- **Armazenamento anual**:
  460 MB \* 365 = **167.9 GB**

---

**Cenário 3**:

- **100 URLs/minuto**
- **Leitura/Escrita Ratio**: 500:1
  - URLs criadas por dia:
    100 \* 60 \* 24 = 144,000
  - Leituras por dia:
    144,000 \* 500 = 72,000,000
- **Armazenamento diário**:
  144,000 \* 3199 = **460,656,000 bytes (~460 MB)**
- **Armazenamento anual**:
  460 MB \* 365 = **167.9 GB**

---

**Cenário 4**:

- **1000 URLs/minuto**
- **Leitura/Escrita Ratio**: 500:1
  - URLs criadas por dia:
    1000 \* 60 \* 24 = 1,440,000
  - Leituras por dia:
    1,440,000 \* 500 = 720,000,000
- **Armazenamento diário**:
  1,440,000 \* 3199 = **4,606,560,000 bytes (~4.6 GB)**
- **Armazenamento anual**:
  4.6 GB \* 365 = **1.67 TB**

---

## **2.1 Armazenamento no Cache**

- **80% das URLs serão usadas**
- TTL do cache: **24h**

1. **Cenário 1**:
   URLs no cache:
   14,400 \* 0.8 = 11,520
   Cache diário:
   11,520 \* 2089 = 24,040,320 bytes (~24 MB)

2. **Cenário 2**:
   URLs no cache:
   144,000 \* 0.8 = 115,200
   Cache diário:
   115,200 \* 2089 = 240,403,200 bytes (~240 MB)

3. **Cenário 3**:
   URLs no cache:
   144,000 \* 0.8 = 115,200
   Cache diário:
   115,200 \* 2089 = 240,403,200 bytes (~240 MB)

4. **Cenário 4**:
   URLs no cache:
   1,440,000 \* 0.8 = 1,152,000
   Cache diário:
   1,152,000 \* 2089 = 2,404,032,000 bytes (~2.4 GB)

---

## **2.2 Largura de Banda (Bandwidth)**

### **Cálculo**

- Cada leitura envolve: 2089 bytes (hash + target URL)
- Cada escrita envolve: 3199 bytes (completo)

1. **Cenário 1**:

   - Leituras: 720,000 \* 2089 = 1.5 GB
   - Escritas: 14,400 \* 3199 = 46 MB
   - Total: 1.55 GB

2. **Cenário 2**:

   - Leituras: 7,200,000 \* 2089 = 15 GB
   - Escritas: 144,000 \* 3199 = 460 MB
   - Total: 15.46 GB

3. **Cenário 3**:

   - Leituras: 72,000,000 \* 2089 = 150 GB
   - Escritas: 144,000 \* 3199 = 460 MB
   - Total: 150.46 GB

4. **Cenário 4**:
   - Leituras: 720,000,000 \* 2089 = 1.5 TB
   - Escritas: 1,440,000 \* 3199 = 4.6 GB
   - Total: 1.5046 TB

---

## **Resumo Final**

| Cenário | Banco de Dados (1 ano) | Cache (24h) | Bandwidth Diário |
| ------- | ---------------------- | ----------- | ---------------- |
| 1       | 16.8 GB                | 24 MB       | 1.55 GB          |
| 2       | 167.9 GB               | 240 MB      | 15.46 GB         |
| 3       | 167.9 GB               | 240 MB      | 150.46 GB        |
| 4       | 1.67 TB                | 2.4 GB      | 1.5046 TB        |
