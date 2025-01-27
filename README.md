# Project 111 - URL Shortener

URL Shortener - Encurtador de URL

## Estrutura da API

```
src
├── main
│   ├── java
│   │   └── com.jeanbarcellos.project111
│   │       ├── controller
│   │       │   ├── UrlShortenerController.java
│   │       │   ├── UrlRedirectController.java
│   │       │   ├── UrlExpiredCleanerController.java
│   │       │   └── UserController.java
│   │       ├── dto
│   │       │   ├── UrlRequest.java
│   │       │   ├── UrlResponse.java
│   │       │   ├── UserRequest.java
│   │       │   └── UserResponse.java
│   │       ├── entity
│   │       │   ├── Url.java
│   │       │   └── User.java
│   │       ├── mapper
│   │       │   ├── UrlMapper.java
│   │       │   └── UserMapper.java
│   │       ├── repository
│   │       │   ├── UrlRepository.java
│   │       │   └── UserRepository.java
│   │       ├── scheduler
│   │       │   └── UrlExpiredCleanerScheduler.java
│   │       ├── service
│   │       │   ├── UrlShortenerService.java
│   │       │   ├── UrlRedirectService.java
│   │       │   ├── UrlExpiredCleanerService.java
│   │       │   └── UserService.java
│   │       └── UrlShortenerApplication.java
│   └── resources
│       └── db
│           └── migration
│               └── migrations.sql
└── test

```

## Plano de capacidade

- [Plano de Capacidade - Table URLs](doc/plano-capacidade-urls.md)
- [Planilha de Cálculo](doc/calc.ods)
