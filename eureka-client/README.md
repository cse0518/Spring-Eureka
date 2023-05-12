# Spring Eureka Client

Eureka Client 구현

<br/>

## Index

- [Eureka Client Settings](#eureka-client-settings)
  - [Dependency](#dependency)
  - [@EnableDiscoveryClient](#enablediscoveryclient)
  - [Property](#property)
- [WebFlux Settings](#webflux-settings)
  - [WebFlux Configuration](#webflux-configuration)
  - [Rest Controller](#rest-controller)
- [Kafka Setting](#kafka-setting)
  - [Kafka Producer](#kafka-producer)

<br/>

## Eureka Client Settings

### Dependency

- 처음 spring initialization 할 때 설정을 해줘야 에러가 나지 않음.
- `WebFlux`, `Eureka Discovery Client`, `Kafka` dependencies 추가
```groovy
dependencies {
	// WebFlux
	implementation 'org.springframework.boot:spring-boot-starter-webflux'

	// Eureka Client
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'

	// Kafka
	implementation 'org.springframework.kafka:spring-kafka'
}
```

<br/>

### @EnableDiscoveryClient

- application 클래스에 `@EnableDiscoveryClient` 추가
  - eureka client로 등록
  ```java
  @SpringBootApplication
  @EnableDiscoveryClient
  public class EurekaClientApplication {
      public static void main(String[] args) {
          SpringApplication.run(EurekaClientApplication.class, args);
      }
  }
  ```

<br/>

### Property

- property 설정 (application.yml)
  ```yaml
  spring:
    application:
      name: eureka-client
    kafka:
      bootstrapAddress: kafka:9092
      topic:
        name: testTopic
  
  eureka:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka # Eureka Server
  ```

<br/>

## WebFlux Settings

### WebFlux Configuration

- HTTP 요청을 비동기적으로 처리하기 위해 WebFlux 사용

```java
import com.humuson.eurekaclient.controller.TestRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFlux
@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Bean
    public RouterFunction<ServerResponse> route(TestRestController controller) {
        return RouterFunctions.route()
                .GET("/service1/status", controller::getStatus)
                .POST("/service1/{userId}", RequestPredicates.accept(MediaType.APPLICATION_JSON), controller::order)
                .build();
    }
}
```

<br/>

### Rest Controller

- 간단한 Rest Controller 구현
  ```java
  import com.humuson.eurekaclient.dto.TestDto;
  import com.humuson.eurekaclient.kafka.KafkaProducer;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;
  import org.springframework.web.reactive.function.server.ServerRequest;
  import org.springframework.web.reactive.function.server.ServerResponse;
  import reactor.core.publisher.Mono;
  
  import java.net.URI;
  
  @RestController
  @RequestMapping("/service1")
  public class TestRestController {
  
      @Value("${server.port}")
      private String port;
    
      private final KafkaProducer kafkaProducer;
    
      public TestRestController(KafkaProducer kafkaProducer) {
          this.kafkaProducer = kafkaProducer;
      }
    
      @GetMapping("/status")
      public Mono<ServerResponse> getStatus(ServerRequest request) {
          return ServerResponse.ok().bodyValue("Service1 server is running on port : " + port);
      }
    
      @PostMapping("/{userId}")
      public Mono<ServerResponse> order(ServerRequest request) {
          String userId = request.pathVariable("userId");
          // User user = repository.findById(userId);
      
          Mono<TestDto> testDto = request.bodyToMono(TestDto.class);
          // repository.save(testDto.toEntity());
      
          return testDto.flatMap(dto -> {
              String data;
              try {
                  data = objectMapper.writeValueAsString(dto);
              } catch (JsonProcessingException e) {
                  return Mono.error(new RuntimeException(e));
              }
      
              kafkaProducer.publish(data);
              return ServerResponse.created(URI.create("/service1/order/" + dto.getOrderId())).build();
          });
      }
  }
  ```

<br/>

## Kafka Setting

### Kafka Producer

- 해당 kafka 토픽으로 데이터 publish
  ```java
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.kafka.core.KafkaTemplate;
  import org.springframework.kafka.support.SendResult;
  import org.springframework.stereotype.Component;
  import org.springframework.util.concurrent.ListenableFuture;
  import org.springframework.util.concurrent.ListenableFutureCallback;
  
  @Slf4j
  @Component
  public class KafkaProducer {
  
      @Value(value = "${spring.kafka.topic.name}")
      private String topicName;
  
      private final KafkaTemplate<String, String> kafkaTemplate;
  
      public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
          this.kafkaTemplate = kafkaTemplate;
      }
  
      public void publish(String message) {
          ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
  
          future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
              @Override
              public void onSuccess(SendResult<String, String> result) {
                  String data = result.getProducerRecord().value();
                  long offset = result.getRecordMetadata().offset();
                  log.info("Sent message=[{}] with offset=[{}]", data, offset);
              }
  
              @Override
              public void onFailure(Throwable ex) {
                  log.info("Unable to send message - due to : {}", ex.getMessage());
              }
          });
      }
  }
  ```
