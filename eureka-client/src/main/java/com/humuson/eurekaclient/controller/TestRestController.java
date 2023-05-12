package com.humuson.eurekaclient.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

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
