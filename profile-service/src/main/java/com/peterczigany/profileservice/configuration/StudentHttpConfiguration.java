package com.peterczigany.profileservice.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.validator.StudentPatchValidator;
import com.peterczigany.profileservice.validator.StudentPostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Configuration
public class StudentHttpConfiguration {

  @Autowired private StudentController controller;

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route()
        .GET("/students", request -> ok().body(controller.getAllStudents(), Student.class))
        .POST("/students", this::handlePostRequest)
        .PATCH("/students/{id}", this::handlePatchRequest)
        .build();
  }

  private Mono<ServerResponse> handlePostRequest(ServerRequest request) {
    Validator validator = new StudentPostValidator();
    Mono<StudentDTO> requestMono = request.bodyToMono(StudentDTO.class);
    Mono<Student> responseBody =
        requestMono.flatMap(
            body -> {
              Errors errors = new BeanPropertyBindingResult(body, StudentDTO.class.getName());
              validator.validate(body, errors);
              if (errors.getAllErrors().isEmpty()) {
                return controller.save(body);
              } else {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errors.getAllErrors().toString());
              }
            });
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(responseBody, Student.class);
  }

  private Mono<ServerResponse> handlePatchRequest(ServerRequest request) {
    Validator validator = new StudentPatchValidator();
    Mono<StudentDTO> requestMono = request.bodyToMono(StudentDTO.class);
    Mono<Student> responseBody =
        requestMono.flatMap(
            body -> {
              Errors errors = new BeanPropertyBindingResult(body, StudentDTO.class.getName());
              validator.validate(body, errors);
              if (errors.getAllErrors().isEmpty()) {
                return controller.updateStudent(request.pathVariable("id"), body);
              } else {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errors.getAllErrors().toString());
              }
            });
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(responseBody, Student.class);
  }
}
