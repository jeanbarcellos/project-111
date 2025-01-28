# URL Shortener System Design

## **Componentes Principais**

1. **Usuário**

   - Interface do cliente para criar e redirecionar URLs (Web ou API).

2. **Web Application Firewall (WAF)**

   - Protege contra ataques como SQL Injection, Cross-Site Scripting (XSS) e ataques DDoS.
   - Configurado para validar todas as solicitações antes de atingir o Load Balancer.
   - **AWS Resource:** AWS WAF integrado ao Elastic Load Balancer ou Amazon CloudFront.

3. **Load Balancer**

   - Distribui solicitações entre as instâncias do serviço.
   - **AWS Resource:** Elastic Load Balancer (ELB).

4. **Servidores de Aplicação**

   - Stateless.
   - Hospedam as APIs REST para:
     - Criar uma URL curta.
     - Redirecionar para a URL original.
     - Gerenciar a lógica de expiração.
   - **AWS Resource:** Amazon ECS ou AWS Lambda (Serverless).

5. **Cache Distribuído**

   - Redis para armazenar os mapeamentos `shortUrl -> longUrl` mais acessados.
   - Configurado com TTL para sincronização com o banco de dados.
   - **AWS Resource:** Amazon ElastiCache (Redis).

6. **Banco de Dados Relacional (PostgreSQL)**

   - Cluster com replicação:
     - Master: Gerencia operações de escrita.
     - Slaves: Processam operações de leitura.
   - **AWS Resource:** Amazon RDS for PostgreSQL.
   - Estruturas:
     - Tabela `user`:
       ```sql
       CREATE TABLE project111.user (
           id UUID NOT NULL,
           name VARCHAR(255) NOT NULL,
           email VARCHAR(255) NOT NULL,
           password VARCHAR(255) NOT NULL,
           created_at TIMESTAMP(0) NOT NULL,
           CONSTRAINT user_email_uk UNIQUE (email),
           PRIMARY KEY (id)
       );
       ```
     - Tabela `url`:
       ```sql
       CREATE TABLE project111.url (
           hash VARCHAR(6),
           target_url VARCHAR NOT NULL,
           created_at TIMESTAMP(6) NOT NULL,
           expires_at DATE NOT NULL,
           user_id UUID NOT NULL,
           PRIMARY KEY (hash),
           CONSTRAINT url_user_id_fk FOREIGN KEY (user_id) REFERENCES project111.user
       );
       ```

7. **Sharding**

   - Se o volume de dados crescer significativamente:
     - Divisão dos dados entre múltiplas instâncias do banco.
     - Hash do `shortUrl` para determinar o shard.
   - **AWS Resource:** Implementação customizada com Amazon RDS ou Amazon Aurora.

8. **CDN (Content Delivery Network)**

   - Cacheia redirecionamentos de URLs de alta demanda.
   - Reduz a latência para usuários geograficamente distantes.
   - **AWS Resource:** Amazon CloudFront.

9. **Telemetria e Observabilidade**

   - **New Relic**:
     - Substitui ou complementa o uso de Prometheus, Grafana e AWS X-Ray.
     - Monitoramento de performance de aplicação (APM) para identificar gargalos e otimizar desempenho.
     - Configurado nos servidores de aplicação para rastreamento de transações e métricas em tempo real.
     - Integração com Redis, RDS e ElastiCache para monitoramento de uso e desempenho.
     - Dashboards para métricas de negócios, como URLs criadas e taxa de redirecionamento.
   - **Elastic Stack (ELK)**: Centraliza e analisa logs estruturados.
   - **AWS Resources:**
     - Amazon CloudWatch (logs, métricas e alarmes).

10. **Autenticação e Autorização**
    - API segura com autenticação JWT para gerenciar acessos por usuário.
    - Relacionamento entre usuários e URLs garantindo que cada usuário só possa gerenciar suas próprias URLs.

## **Fluxo de Dados**

1. **Criação de URL**

   - O usuário envia uma solicitação com a URL longa e seu token JWT para autenticação.
   - Solicitação passa pelo AWS WAF para validação de segurança.
   - Load balancer redireciona para um servidor de aplicação.
   - O servidor:
     - Valida o token JWT e extrai o `userId`.
     - Gera uma `shortUrl` usando uma sequência do banco e codifica com Base62.
     - Salva `shortUrl`, `longUrl`, `userId` e `expiresAt` no banco de dados.
     - Insere o mapeamento no Redis com TTL (igual à data de expiração).
     - Envia métricas de performance e transação ao New Relic.
   - Retorna a `shortUrl` ao cliente.

2. **Redirecionamento**

   - O usuário acessa a `shortUrl`.
   - Solicitação passa pelo AWS WAF para validação de segurança.
   - Load balancer redireciona para um servidor de aplicação.
   - O servidor:
     - Consulta o Redis para o mapeamento.
       - Cache hit: Redireciona para a `longUrl` imediatamente.
       - Cache miss: Busca no banco de dados e atualiza o Redis.
     - Envia métricas de latência e erros ao New Relic.
   - Retorna um redirecionamento HTTP 302 para o cliente.

3. **Autenticação de Usuário**
   - O cliente realiza login fornecendo email e senha.
   - Servidor valida as credenciais contra o banco de dados.
   - Se válido, retorna um token JWT assinado ao cliente.

## **Diagrama do Sistema**

- **Usuário:** Cliente Web/API.
- **WAF:** Valida solicitações antes de atingir o Load Balancer (AWS WAF).
- **Load Balancer:** Direciona para servidores de aplicação (Elastic Load Balancer).
- **Servidores de Aplicação:** Implementam a lógica de criação e redirecionamento de URLs (Amazon ECS ou AWS Lambda).
- **Redis Cache:** Otimiza consultas de redirecionamento frequentes (Amazon ElastiCache).
- **PostgreSQL Cluster:** Banco de dados primário, com replicação para leitura/escrita (Amazon RDS for PostgreSQL).
- **Shards (Opcional):** Escalam o banco de dados para volumes maiores (Amazon RDS ou Aurora).
- **CDN:** Acelera o redirecionamento de URLs populares (Amazon CloudFront).
- **Monitoramento:** Inclui New Relic para APM e métricas de negócios, Elastic Stack para logs e Amazon CloudWatch para alarmes.
- **Autenticação:** Adiciona suporte a JWT para autenticação segura de usuários.
