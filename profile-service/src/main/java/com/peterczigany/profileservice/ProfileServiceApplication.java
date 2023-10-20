package com.peterczigany.profileservice;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan(basePackages = "com.peterczigany.profileservice")
public class ProfileServiceApplication {

//  @Bean
//  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//
//    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//    initializer.setConnectionFactory(connectionFactory);
//    initializer.setDatabasePopulator(
//        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//
//    return initializer;
//  }

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
