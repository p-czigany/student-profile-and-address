package com.peterczigany.profileservice;

import com.peterczigany.profileservice.configuration.StudentHttpConfiguration;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest
@Import(StudentHttpConfiguration.class)
class StudentHttpTest {

  @MockBean private StudentRepository repository;

  @Autowired private WebTestClient client;

  @Test
  void getAllStudents() throws Exception {

    Mockito.when(this.repository.findAll())
        .thenReturn(
            Flux.just(
                new Student(
                    UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                    "Joe",
                    "joe@school.com"),
                new Student(
                    UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                    "Ashley",
                    "ashley@school.com")));

    client
        .get()
        .uri("http://localhost:8080/students")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("@.[0].name")
        .isEqualTo("Joe")
        .jsonPath("@.[1].name")
        .isEqualTo("Ashley");
  }


}
