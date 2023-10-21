package com.peterczigany.profileservice.configuration;

// import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import com.peterczigany.profileservice.validator.StudentValidator;
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

  @Autowired StudentRepository repository;

  @Bean
  RouterFunction<ServerResponse> routes(/*@Autowired StudentRepository repository*/ ) {
    return route()
        .GET("/students", request -> ok().body(repository.findAll(), Student.class))
        .POST("/students", this::handlePostRequest)
        .build();
  }

  private Mono<ServerResponse> handlePostRequest(ServerRequest request) {
    Validator validator = new StudentValidator();
    Mono<Student> requestMono = request.bodyToMono(Student.class);
    Mono<Student> responseBody =
        requestMono.flatMap(
            body -> {
              Errors errors = new BeanPropertyBindingResult(body, Student.class.getName());
              validator.validate(body, errors);
              if (errors.getAllErrors().isEmpty()) {
                return requestMono.flatMap(repository::save);
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
