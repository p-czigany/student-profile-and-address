package com.peterczigany.profileservice.configuration;

//import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class StudentHttpConfiguration {

  @Bean
  RouterFunction<ServerResponse> routes(@Autowired StudentRepository repository) {
    return route()
        .GET("/students", request -> ok().body(repository.findAll(), Student.class))

        .POST("/students", request -> {
          Mono<Student> studentMono = request.bodyToMono(Student.class);
          return studentMono
              .flatMap(repository::save)
              .flatMap(savedStudent -> ok().bodyValue(savedStudent));
        })
        
        .build();
  }

//  .POST("/students", request -> created(URI.create("/students")).build())
//      .build();

//  @Bean
//  RouterFunction<ServerResponse> composedRoutes(@Autowired StudentRepository repository) {
//    return
//        route(GET("/students"),
//            req -> ok().body(
//                repository.findAll(), Student.class))
//
//            .and(route(GET("/students/{id}"),
//                req -> ok().body(
//                    repository.findById(UUID.fromString(req.pathVariable("id"))), Student.class)))
//
//            .and(route(POST("/students"),
//                req -> req.body(toMono(Student.class))
//                    .doOnNext(repository::save)
//                    .then(ok().build())));
//  }
}
