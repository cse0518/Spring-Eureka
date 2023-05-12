package com.humuson.springcloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayRouting {

    @Bean
    public RouteLocator configurationRoute(RouteLocatorBuilder rlb) {
        return rlb.routes()
                .route("id", r -> r.path("/service1/**").uri("lb://eureka-client"))
                .route("id", r -> r.path("/service2/**").uri("lb://eureka-client-2nd"))
                .build();
    }
}
