2. Telemetria e Métricas de Negócio
   Adicionar telemetria e métricas permitirá monitorar o comportamento do sistema e tomar decisões baseadas em dados.

2.1 Ferramentas Recomendadas

- Prometheus: Para coletar métricas numéricas, como contagem de URLs encurtadas, leituras, ou tempo de resposta.
- Grafana: Para visualizar gráficos e dashboards das métricas coletadas.
- Micrometer: Integração com Prometheus para exportar métricas diretamente do código Java.

  2.2 Métricas de Negócio

- URLs criadas por minuto.
- Taxa de redirecionamento por minuto.
- Taxa de cache hit/miss (Redis).
- Tempo médio de resposta (latência) para criação e redirecionamento de URLs

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

```java
@Service
public class UrlShortenerService {
private final MeterRegistry meterRegistry;

    public UrlShortenerService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public String createShortUrl(String longUrl) {
        meterRegistry.counter("url.shortener.created").increment();
        // lógica de criação de URL
    }

    public String getLongUrl(String shortUrl) {
        meterRegistry.counter("url.shortener.redirect").increment();
        // lógica de redirecionamento
    }

}
```

3. Observabilidade e Logs

3.1 Logs

- Use ferramentas como Elastic Stack (ELK) ou OpenSearch para centralizar e gerenciar logs.
- Formato estruturado:
  - JSON estruturado facilita a análise.
  - Exemplo de log:
    ```java
    {
    "timestamp": "2025-01-10T12:00:00Z",
    "level": "INFO",
    "service": "url-shortener",
    "operation": "createShortUrl",
    "shortUrl": "abc123",
    "longUrl": "https://example.com",
    "latencyMs": 120
    }
    ```
- Níveis de log recomendados:
  - `DEBUG`: Para informações detalhadas de desenvolvimento.
  - `INFO`: Para eventos normais do sistema.
  - `ERROR`: Para falhas e exceções.
- Integração com Ferramentas

  - **Loki** (para logs centralizados).
  - **Kibana** (para dashboards de logs).

    3.2 Tracing (Rastreabilidade)

- Utilize OpenTelemetry para rastrear requisições do início ao fim no sistema distribuído.
- Exemplo de implementação:
  - Adicione um identificador único (trace ID) em cada requisição e propague-o entre os serviços.


-