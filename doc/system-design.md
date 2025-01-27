### **URL Shortener - System Design Atualizado**

Abaixo está o resumo do sistema atualizado com as últimas alterações incorporadas, considerando a separação de responsabilidades entre microsserviços, uso de cache, métricas e observabilidade.

---

### **Componentes Principais**

1. **Usuário**

   - Clientes acessando a API para criar, redirecionar e gerenciar URLs.
   - Interface pode ser Web, Mobile ou CLI.

2. **Web Application Firewall (WAF)**

   - Proteção contra ataques como SQL Injection, XSS e DDoS.
   - Validando requisições antes de chegar ao Load Balancer.
   - **AWS Resource:** AWS WAF.

3. **Load Balancer (ELB)**

   - Distribuição de tráfego entre microsserviços.
   - **AWS Resource:** Elastic Load Balancer.

4. **Microsserviços**

   - **UrlShortener Service**:
     Responsável por gerar URLs encurtadas, validar entrada e armazenar no banco.
   - **UrlRedirect Service**:
     Responsável por redirecionar para a URL original com baixa latência. Inclui cache.
   - **UrlExpiredCleaner Service**:
     Serviço responsável por deletar URLs expiradas, com endpoints manuais e agendamento automático.

5. **Cache Distribuído (Redis)**

   - Armazena o mapeamento `shortUrl -> longUrl` para redirecionamentos frequentes, com TTL sincronizado com a expiração da URL.
   - **AWS Resource:** Amazon ElastiCache (Redis).

6. **Banco de Dados Relacional (PostgreSQL)**

   - Cluster replicado para operações de leitura/escrita:
     - **Master Node**: Especificamente para gravações.
     - **Read Replicas**: Otimizado para consultas.
   - **AWS Resource:** Amazon RDS (PostgreSQL).

7. **Armazenamento de Logs e Métricas**

   - **Elastic Stack (ELK)**: Centraliza logs estruturados para análise e auditoria.
   - **Prometheus & Grafana**: Coleta métricas de sistemas, como latência, uso de CPU, hits/misses no cache.
   - **New Relic APM**: Rastreia transações para monitoramento em tempo real.
   - **AWS CloudWatch**: Alarmes e logs adicionais.

---

### **Fluxos de Dados**

#### **1. Criação de URL Encurtada**

- Usuário envia a URL longa para o **UrlShortener Service**.
- O serviço:
  1. Gera um hash único para a URL.
  2. Verifica conflitos no banco de dados.
  3. Salva no PostgreSQL e insere no Redis com TTL.
  4. Retorna a URL encurtada.
- Métricas da operação são enviadas ao **New Relic**.

---

#### **2. Redirecionamento**

- Usuário acessa uma URL curta.
- O **UrlRedirect Service**:
  1. Consulta o Redis para encontrar a URL longa.
     - **Cache hit**: Redireciona imediatamente (HTTP 302).
     - **Cache miss**: Busca no banco de dados, atualiza o Redis e redireciona.
  2. Registra métricas como número de redirecionamentos no banco.
  3. Métricas de cache hits/misses enviadas para **Prometheus** e **Grafana**.

---

#### **3. Exclusão de URLs Expiradas**

- **UrlExpiredCleaner Service**:
  - Executa um processo agendado diariamente:
    1. Consulta URLs expiradas no banco de dados.
    2. Remove os registros do PostgreSQL e Redis.
  - Possui endpoint para exclusão manual de URLs expiradas.

---

### **Recursos para Confiabilidade e Escalabilidade**

1. **Banco de Dados**

   - Replicação (read replicas) para separar leitura e escrita.
   - Sharding opcional para alta escalabilidade no futuro.

2. **Auto-Scaling**

   - **Microsserviços**: Escalam horizontalmente de acordo com métricas de CPU e latência.
   - **Redis**: Cluster com partições para grandes volumes de cache.
   - **RDS**: Configurado com read replicas para lidar com altos volumes de leitura.

3. **Monitoramento e Observabilidade**

   - Logs centralizados (ELK Stack).
   - Métricas detalhadas com Prometheus, Grafana e New Relic.
   - Dashboards para análise de performance e comportamento de negócios.

4. **Resiliência**
   - Failover automático no RDS.
   - Redis configurado em modo cluster para tolerância a falhas.
   - WAF para mitigar ataques.

---

### **Diagrama Atualizado**

```plaintext
                   +--------------------+
                   |     Usuário        |
                   +--------------------+
                            |
                            v
                  +---------------------+
                  |        WAF          |
                  +---------------------+
                            |
                            v
                  +---------------------+
                  |   Load Balancer     |
                  +---------------------+
                            |
    +-----------------------+-----------------------+
    |                       |                       |
+----------+         +------------+          +----------------+
| Shortener|         | Redirect   |          | Expired Cleaner|
| Service  |         | Service    |          | Service        |
+----------+         +------------+          +----------------+
    |                       |                       |
    +                       +                       +
+---------------------------------+    +--------------------+
|           Redis Cache           |    | PostgreSQL Cluster |
+---------------------------------+    +--------------------+
    |                                           |
    +-------------------------------------------+
                |
        +---------------------+
        |        Logs         |
        |  (Elastic Stack)    |
        +---------------------+
```

---

### **Recomendações Finais**

1. **Backup**

   - Configurar snapshots automáticos no RDS e Redis para recuperação de dados.

2. **Segurança**

   - Implementar HTTPS para todas as conexões.
   - Gerenciamento de segredos (AWS Secrets Manager).

3. **Testes de Performance**

   - Realizar testes de carga simulando picos de tráfego e cenários de falhas.

4. **Melhoria Contínua**
   - Avaliar o comportamento do TTL no cache.
   - Automatizar a verificação de URLs inválidas (e.g., dead links).
