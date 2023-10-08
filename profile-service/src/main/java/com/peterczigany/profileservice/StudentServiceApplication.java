package com.peterczigany.profileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class StudentServiceApplication {

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  WebClient webClient() {
    return webClientBuilder().build();
  }

  public static void main(String[] args) {
    SpringApplication.run(StudentServiceApplication.class, args);
  }

}
