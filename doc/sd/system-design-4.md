## **System Design**

Aqui está o **System Design** atualizado considerando a implementação mais recente e as novas funcionalidades:

---

### **1. Arquitetura Geral**

**Tipo de Arquitetura:** Microserviços simplificados, baseados em Spring Boot, integrados a um banco de dados PostgreSQL e utilizando ferramentas de observabilidade e cache.

**Componentes do Sistema:**

1. **API Gateway (opcional, para escalabilidade futura):**

   - Pode ser adicionado para roteamento e balanceamento de carga.

2. **Microserviços Principais:**

   - **UrlShortenerService:**
     - Responsável pela criação de URLs encurtadas.
     - Gera hashes únicos e os armazena no banco de dados.
   - **UrlRedirectService:**
     - Trata redirecionamentos HTTP para as URLs originais a partir dos hashes.
   - **UrlExpiredCleanerService:**
     - Gerencia a exclusão de URLs expiradas.
     - Inclui um endpoint para exclusão manual e um processo agendado.

3. **Banco de Dados Relacional:**

   - PostgreSQL.
   - Contém duas tabelas principais (`user` e `url`).
   - Dados são versionados e migrados usando Flyway.

4. **Cache:**

   - Redis (ou Memcached) para melhorar a performance das consultas relacionadas a redirecionamento.
   - 80% das URLs mais acessadas são armazenadas com TTL de 24 horas.

5. **Ferramentas de Observabilidade:**

   - **Logs:** Elastic Stack para centralização e análise.
   - **Métricas:** Prometheus + Grafana.
   - **Tracing:** New Relic (APM) para monitoramento de latência e execução.

6. **Segurança:**
   - Autenticação e autorização básica para endpoints críticos (futuro).
   - Integração com um gerenciador de segredos (como AWS Secrets Manager) para credenciais e chaves.

---

### **2. Fluxo de Trabalho**

1. **Criação de URLs:**

   - O cliente envia uma requisição HTTP POST com os dados da URL e o ID do usuário.
   - O `UrlShortenerService` gera o hash, salva no banco de dados e retorna a URL encurtada.

2. **Redirecionamento:**

   - O cliente acessa a URL encurtada.
   - O `UrlRedirectService` verifica o cache:
     - Se a URL está no cache, redireciona imediatamente.
     - Caso contrário, consulta o banco de dados, armazena no cache e redireciona.

3. **Limpeza de URLs Expiradas:**
   - O `UrlExpiredCleanerService` verifica URLs expiradas no banco de dados.
   - URLs expiradas são excluídas manualmente via endpoint ou automaticamente por um job agendado.

---

### **3. Diagrama de Componentes**

```
          +---------------------+               +--------------------+
          |   Client (Browser)  |  ---------->  | API Gateway (NGINX)|
          +---------------------+               +--------------------+
                                                       |
                     +-----------------------------+-----------------------------+
                     |                                                           |
        +---------------------------+                         +---------------------------+
        | UrlShortenerService       |                         | UrlRedirectService        |
        | - POST /api/shortener     |                         | - GET /api/redirect/{hash}|
        +---------------------------+                         +---------------------------+
                     |                                                           |
        +---------------------------------------------+       +---------------------------------------------+
        | PostgreSQL                                   |       | Cache (Redis/Memcached)                    |
        | - Tables: user, url                          |       | - 80% of frequently used URLs              |
        | - Stores all URL and user data               |       | - TTL: 24 hours                            |
        +---------------------------------------------+       +---------------------------------------------+
                     |
        +---------------------------+
        | UrlExpiredCleanerService  |
        | - DELETE /api/cleaner     |
        | - Scheduled Job (3 AM)    |
        +---------------------------+
```

---

### **4. Requisitos Atendidos**

#### **Requisitos Funcionais:**

1. **Criação de URLs encurtadas:**

   - Implementado com endpoint POST.
   - URLs encurtadas possuem validade de 30 dias.

2. **Redirecionamento:**

   - Endpoint GET para redirecionar URLs a partir do hash.

3. **Exclusão de URLs expiradas:**
   - Endpoint DELETE manual.
   - Processo agendado para exclusão automática.

#### **Requisitos Não Funcionais:**

1. **Escalabilidade:**

   - Redis/Memcached adicionados para reduzir a carga no banco.
   - Estrutura de serviços independentes facilita o aumento de réplicas.

2. **Desempenho:**

   - Uso de cache reduz o tempo de leitura para URLs mais acessadas.
   - Consultas otimizadas ao banco de dados.

3. **Disponibilidade:**

   - Failover pode ser implementado para PostgreSQL.
   - Serviços podem ser escalados horizontalmente com Kubernetes.

4. **Observabilidade:**
   - Logs centralizados via Elastic Stack.
   - Monitoramento de métricas com Prometheus e Grafana.

---

### **5. Estratégia de Auto-Scale**

1. **Baseado em Métricas:**

   - **CPU e Memória:**
     - Adicionar réplicas automaticamente quando o uso de CPU/memória exceder 70% por mais de 5 minutos.
   - **Throughput de Requisições:**
     - Aumentar réplicas quando o número de requisições ultrapassar 1000 RPM.

2. **Implementação:**
   - Configuração de Kubernetes Horizontal Pod Autoscaler (HPA).
   - Escalar o banco de dados com shards ou read replicas.

---

### **6. Recomendação Adicional**

1. **Segurança Avançada:**

   - Usar OAuth2 para autenticação.
   - Limitar a taxa de requisições (rate limiting) por IP para evitar abusos.

2. **Caching Distribuído:**

   - Implementar cache distribuído (como Redis Cluster) para melhorar a escalabilidade em múltiplas regiões.

3. **CI/CD:**

   - Automatizar pipelines de build, teste e deploy com ferramentas como GitHub Actions, Jenkins ou GitLab CI/CD.

4. **Backup:**
   - Implementar backup periódico para o banco de dados.
   - Garantir recuperação rápida (RTO/RPO) em caso de falhas.

---

Caso precise ajustar ou detalhar algum aspecto do design, é só avisar!
