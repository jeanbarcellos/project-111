# Vídeo MELI - System Design no Mercado Livre na prática

https://www.youtube.com/watch?v=FgeqYkqMqcw

https://app.diagrams.net/#G1moVOywIG3WcR3NyUkUbzs8YEQPT3m5dG#%7B%22pageId%22%3A%22W_RGWpp1l_H5rlz5NVuH%22%7D

## Requisitos

### Requisitos funcionais:

- Usuário informa a URL de entrada devolver de saida
- Retornar 6 caracteres fixos
- Quando a URL for chamada, o serviço deve redirecionar para a URL original
  URLS curtas devem expirar em 1 ano

### Requisitos não funcionais

- Links não podem ser advinhavel
- Redirecionamento tem que ser em tempo real e no minimo de latencia possivel

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

## Amazenamento estimado

Calculo de armazenamento

### Schema

```sql
CREATE SCHEMA IF NOT EXISTS "project111";

CREATE TABLE project111.user (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(0) NOT NULL,
    CONSTRAINT user_email_uk UNIQUE (email),
    PRIMARY KEY (id)
);

CREATE TABLE project111.url (
    hash VARCHAR(6),
    target_url VARCHAR NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    expires_at DATE NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (hash)
);

ALTER TABLE IF EXISTS project111.url
    ADD CONSTRAINT url_user_id_fk FOREIGN KEY (user_id) REFERENCES project111.user;
```

### Redirecionamentos

Há duas opções básicas de redirecionamento em HTTP:

- **Status code 301** – indicando que o recurso endereçado pela URL solicitada foi movido permanentemente para um novo endereço;
- **Status code 302** – indicado que o recurso endereçado pela URL solicitada foi movido temporariamente para um novo endereço

Retornar 301 é, de longe, a forma mais econômica de implementar o redirecionamento, afinal, evitará que os servidores do serviço sejam acionados desnecessariamente, uma vez que o browser cliente deverá promover o redirecionamento sozinho.

Retornar 302, por outro lado, não diminui a quantidade de acionamentos, mas habilita fazer tracking de acesso e gerar estatísticas (algo desnecessário, conforme o negócio)
