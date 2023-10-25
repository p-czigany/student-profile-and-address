package com.peterczigany.profileservice.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class StudentHttpConfiguration {

  @Autowired private StudentController controller;
  @Autowired private StudentPostValidationHandler postValidationHandler;

  @Autowired private StudentPatchValidationHandler patchValidationHandler;

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route()
        .GET("/students", request -> ok().body(controller.getAllStudents(), Student.class))
        .POST(
            "/students",
            accept(MediaType.APPLICATION_JSON),
            request -> postValidationHandler.handleRequest(request))
        .PATCH(
            "/students/{id}",
            accept(MediaType.APPLICATION_JSON),
            request -> patchValidationHandler.handleRequest(request))
        .build();
  }
}
