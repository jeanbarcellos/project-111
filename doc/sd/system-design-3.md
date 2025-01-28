## **1. System Design para Cenário 1 e Cenário 2**

Aqui está uma análise detalhada para atender aos cenários 1 e 2, com foco no **system design**, confiabilidade, disponibilidade, estratégias de auto-escalabilidade e recomendações adicionais:

### **Cenário 1 (10 RPM): Baixa Escala**

- **Componentes:**
  - **API Gateway e WAF:** AWS API Gateway com proteção via AWS WAF.
  - **Load Balancer:** Um Elastic Load Balancer (ELB) para distribuir o tráfego entre instâncias.
  - **Servidores de Aplicação:** 2 instâncias EC2 ou containers ECS para rodar o backend (Spring Boot).
  - **Banco de Dados:** RDS PostgreSQL em uma configuração básica (1 nó primário + 1 réplica para leitura).
  - **Redis Cache:** Cluster ElastiCache com um nó (tamanho pequeno).
  - **Monitoramento e Observabilidade:**
    - New Relic (APM e métricas de negócios).
    - Elastic Stack (logs centralizados).
    - AWS CloudWatch (métricas básicas).
  - **CDN (opcional):** Amazon CloudFront para acelerar redirecionamentos populares.

---

### **Cenário 2 (200 RPM): Alta Escala**

- **Componentes:**
  - **API Gateway e WAF:** AWS API Gateway com AWS WAF para segurança e controle de tráfego.
  - **Load Balancer:** ELB escalável para maior número de instâncias.
  - **Servidores de Aplicação:** ECS Fargate com auto-scaling, ou AWS Lambda (serverless) para responder à demanda de maneira elástica.
  - **Banco de Dados:**
    - RDS PostgreSQL com **replicação horizontal** e sharding para dividir a carga.
    - Configuração com **Multi-AZ** para garantir alta disponibilidade.
  - **Redis Cache:**
    - Cluster Redis ElastiCache com múltiplos nós (para evitar gargalos no cache).
    - Políticas de eviction baseadas em LRU para otimizar memória.
  - **CDN:** Amazon CloudFront obrigatório para redirecionamentos globais de baixa latência.
  - **Monitoramento e Observabilidade:**
    - New Relic (APM).
    - Elastic Stack (logs centralizados).
    - Prometheus + Grafana para métricas e alertas de latência, tráfego e erros.
  - **Mensageria:** SQS ou Kafka, para gerenciar tarefas assíncronas (ex.: eventos de expiração).

---

## **2. Recursos para Garantir Confiabilidade e Disponibilidade**

1. **Alta Disponibilidade (HA):**

   - Utilize **Multi-AZ** no RDS para que o banco esteja replicado em outra zona de disponibilidade.
   - Configure **Redis em modo cluster** com réplicas para evitar falhas no cache.
   - Habilite **Auto Scaling** nos serviços ECS ou EC2.

2. **Escalabilidade Horizontal:**

   - Para os servidores de aplicação, utilize **ECS Fargate** ou AWS Lambda para escalar horizontalmente com base no tráfego.
   - Para o banco de dados, implemente sharding ou particionamento horizontal.

3. **Backup e Recuperação:**

   - Configure **snapshots automáticos** no RDS e Redis para recuperação de desastres.
   - Utilize AWS S3 para armazenamento de backups.

4. **Failover:**

   - Banco de dados e caches configurados com failover automático para uma instância de backup.

5. **Estratégia de Disaster Recovery:**
   - Configure regiões secundárias para failover em casos de falha completa da região principal.

---

## **3. Estratégia de Auto-escale**

### **Aplicação (Servidores de Backend):**

- Configure o auto-scaling com base em métricas como:
  - **CPU Utilization:** Escale se a CPU de servidores atingir 70% ou mais.
  - **Request Count:** Escale com base no número de solicitações recebidas por minuto (ex.: > 200 RPM).
  - **Custom Metrics:** Combine métricas como latência média de redirecionamentos.

### **Redis Cache:**

- Adicione mais nós automaticamente ao cluster quando:
  - Uso de memória atinge 70%.
  - Número de conexões ativas ultrapassa um limite predefinido.

### **Banco de Dados:**

- Escale instâncias de leitura com base em:
  - Taxa de consultas por segundo (QPS).
  - Latência média das consultas.

---

## **4. Recomendações Adicionais**

### **Segurança**

- Configure o **WAF** para bloquear ataques como injeções SQL e tentativas de força bruta.
- Use **IAM Roles** e **KMS** para gerenciar credenciais e dados sensíveis.

### **Telemetria e Logs**

- Centralize todos os logs em um pipeline (Elastic Stack ou AWS CloudWatch).
- Configure **New Relic APM** para identificar gargalos em tempo real.

### **Estratégia de Cache**

- Implementar uma política de invalidação automática para URLs expiradas.
- Utilize **cache prewarming** para URLs populares em campanhas ou picos esperados.

### **Governança**

- Configure alarmes no AWS CloudWatch para:
  - Latência acima de 300ms no redirecionamento.
  - Percentual de erros (ex.: 5XX) acima de 1%.

---

## **Mudanças no System Design**

1. **Separação de Microsserviços:**

   - Um microsserviço dedicado para **escrita** e outro para **leitura/redirecionamento**.
   - O serviço de leitura pode ser otimizado para baixa latência, enquanto o de escrita lida com operações mais complexas (ex.: geração de hash e validação de duplicatas).

2. **Mensageria:**

   - Adicione um **SQS** ou **Kafka** para tarefas como:
     - Processamento de URLs expiradas.
     - Atualização de cache em background.

3. **Monitoramento e Observabilidade:**
   - Inclua **Prometheus e Grafana** para dashboards de infraestrutura.
   - Use **New Relic** para rastreamento detalhado das transações.

---

Se precisar de ajustes ou mais detalhes sobre o design, podemos explorar juntos!
