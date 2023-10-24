package com.peterczigany.profileservice;

import com.peterczigany.profileservice.configuration.StudentHttpConfiguration;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@Import(StudentHttpConfiguration.class)
class StudentHttpTest {

  @MockBean private StudentRepository repository;

  @Autowired private WebTestClient client;

  @Test
  void getAllStudents() throws Exception {

    Mockito.when(repository.findAll())
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

  @Test
  void addNewStudent() throws Exception {
    Student student = new Student(null, "Joe", "joe@school.com");

    Mockito.when(repository.save(student))
        .thenReturn(
            Mono.just(
                new Student(
                    UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                    "Joe",
                    "joe@school.com")));

    client
        .post()
        .uri("http://localhost:8080/students")
        .body(Mono.just(student), Student.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo("Joe")
        .jsonPath("$.email")
        .isEqualTo("joe@school.com");
  }

  @ParameterizedTest
  @CsvSource({"Joe,joe@school@com", ",joe@school.com"})
  void addNewStudentValidationFails(String name, String email) throws Exception {
    Student student = new Student(null, name, email);

    client
        .post()
        .uri("http://localhost:8080/students")
        .body(Mono.just(student), Student.class)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }
}
