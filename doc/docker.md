# Build

## Java MVN

Java Coverage

```bash
mvn clean verify
```

Run Tests

```bash
mvn clean test
```

Generate java package

```bash
mvn clean package -DskipTests
```

## Docker Build

Generate Docker image

```bash
docker image build -t jeanbarcellos/project111_service-api .
```

Push image to docker hub

```bash
docker push jeanbarcellos/project111_service-api
```

## Docker Compose

Up docker-compose

```bash
docker-compose up -d
```

Check status

```bash
docker-compose ps
```

Check logs

```bash
docker logs recargapay_database -f
docker logs recargapay_backend -f
```

Down docker-compose

```bash
docker-compose down
```
