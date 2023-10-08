package com.peterczigany.studentservice;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class StudentHttpConfiguration {

  @Bean
  RouterFunction<ServerResponse> routes(@Autowired StudentRepository repository) {
    return route()
        .GET("/students", request -> ServerResponse.ok().body(repository.findAll()))
//        .POST("/students", RequestPredicates.accept(MediaType.APPLICATION_JSON),
//            request -> ServerResponse.status(HttpStatus.CREATED)
//                .body(repository.save(request.body(Student.class))))
        .build();
  }
}
