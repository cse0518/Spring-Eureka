package com.humuson.eurekaclient2nd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EurekaClient2ndApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClient2ndApplication.class, args);
	}

}
