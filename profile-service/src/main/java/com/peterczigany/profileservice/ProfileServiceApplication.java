package com.peterczigany.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.peterczigany.profileservice")
public class ProfileServiceApplication {

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  WebClient webClient() {
    return webClientBuilder().build();
  }

  public static void main(String[] args) {
    SpringApplication.run(ProfileServiceApplication.class, args);
  }
}
