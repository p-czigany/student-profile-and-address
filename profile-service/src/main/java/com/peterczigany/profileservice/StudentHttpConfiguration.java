package com.peterczigany.profileservice;


import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class StudentHttpConfiguration {

  @Bean
  RouterFunction<ServerResponse> routes(@Autowired StudentRepository repository) {
    return route()
        .GET("/students", request -> ok().body(repository.findAll(), Student.class))
        .build();
  }
}
