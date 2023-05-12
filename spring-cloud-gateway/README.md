## Spring Cloud Gateway 구현

Spring Cloud Gateway 구현

<br/>

## Index

- [API Gateway Settings](#api-gateway-settings)
  - [Dependency](#dependency)
  - [@EnableDiscoveryClient](#enablediscoveryclient)
  - [Property](#property)
  - [Configuration](#configuration)

<br/>

## API Gateway Settings

### Dependency

- `Spring Reactive Web`
  - 비동기, 논블로킹 처리를 위해 spring reactive web(webflux) 사용
- `Eureka Discovery Client`
- `Spring-Cloud-Gateway`

```groovy
dependencies {
    // Webflux
    implementation 'org.springframework.boot:spring-boot-starter-webflux'

    // Gateway
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'

    // Eureka Client
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

<br/>

### @EnableDiscoveryClient

- main 클래스에 `@EnableDiscoveryClient` 추가
  - eureka client로 등록
  ```java
  @SpringBootApplication
  @EnableDiscoveryClient
  public class DiscoveryApplication {
      public static void main(String[] args) {
          SpringApplication.run(DiscoveryApplication.class, args);
      }
  }
  ```

<br/>

### Property

- property 설정 (application.yml)
  ```yaml
  spring:
    application:
      name: spring-gateway

  server:
    port: 8080
  
  eureka:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka # Eureka Server
  ```

<br/>

### Configuration

- Routing Configuration 추가
  - `uri`에 application name 설정
  - 비동기적으로 로드 밸런싱
  ```java
  @Configuration
  public class RoutingConfig {
  
      @Bean
      public RouteLocator configurationRoute(RouteLocatorBuilder rlb) {
    
          return rlb.routes()
                  .route("id", r -> r.path("/client/**").uri("lb://eureka-client"))
                  .route("id", r -> r.path("/client2/**").uri("lb://eureka-client-2nd"))
                  .build();
      }
  }
  ```
