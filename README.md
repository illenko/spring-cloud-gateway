# spring-cloud-gateway

Configurable API Routing Based on API Key.

Technologies:
* Kotlin
* Gradle
* Spring: WebFlux, Cloud Gateway
* Redis
* Docker

Setup:
1. Start Redis:
```
cd src/main/resources/environment
docker-compose up -d
```
2. Start as Spring Boot or build image using "bootBuildImage" task in Gradle.
