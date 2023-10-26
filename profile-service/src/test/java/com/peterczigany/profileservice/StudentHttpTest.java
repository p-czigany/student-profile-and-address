package com.peterczigany.profileservice;

import com.peterczigany.profileservice.configuration.StudentHttpConfiguration;
import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@Import(StudentHttpConfiguration.class)
class StudentHttpTest {

  @MockBean private StudentController controller;

  @Autowired private WebTestClient client;

  @Test
  void getAllStudents() {

    Mockito.when(controller.getAllStudents())
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
  void addNewStudent() {
    StudentDTO student = new StudentDTO("Joe", "joe@school.com");

    Mockito.when(controller.save(student))
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
  void addNewStudentValidationFails(String name, String email) {
    Student student = new Student(null, name, email);

    client
        .post()
        .uri("http://localhost:8080/students")
        .body(Mono.just(student), Student.class)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  void updateStudent() {

    Student baseStudent =
        new Student(
            UUID.fromString("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3"), "Tommy", "test@school.com");
    StudentDTO request = new StudentDTO("Tommy Test", null);

    Student updatedStudent = new Student(baseStudent);
    updatedStudent.setName("Tommy Test");

    Mockito.when(controller.updateStudent("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3", request))
        .thenReturn(
            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updatedStudent));

    client
        .patch()
        .uri("http://localhost:8080/students/1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3")
        .body(Mono.just(new Student(null, "Tommy Test", null)), Student.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3")
        .jsonPath("$.name")
        .isEqualTo("Tommy Test")
        .jsonPath("$.email")
        .isEqualTo("test@school.com");
  }

  @Test
  void updateStudentFails_whenIdNotFound() {
    Student baseStudent =
        new Student(
            UUID.fromString("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3"), "Tommy", "test@school.com");
    StudentDTO request = new StudentDTO("Tommy Test", null);

    Student updatedStudent = new Student(baseStudent);
    updatedStudent.setName("Tommy Test");

    Mockito.when(controller.updateStudent("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3", request))
        .thenReturn(Mono.empty());

    client
        .patch()
        .uri("http://localhost:8080/students/1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3")
        .body(Mono.just(new Student(null, "Tommy Test", null)), Student.class)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ParameterizedTest
  @MethodSource("invalidUpdateData")
  void updateStudentValidationFails(String name, String email) {
    Student student = new Student(null, name, email);

    client
        .patch()
        .uri("http://localhost:8080/students/1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3")
        .body(Mono.just(student), Student.class)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  //  @Test
  //  void deleteStudent() {
  //    Mockito.when(controller.deleteStudent("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3"))
  //        .thenReturn(Mono.just(updatedStudent));
  //    client
  //        .patch()
  //        .uri("http://localhost:8080/students/1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3")
  //        .exchange()
  //        .expectStatus()
  //        .isNoContent();
  //  }

  private static Stream<Arguments> invalidUpdateData() {
    return Stream.of(
        Arguments.of("Joe", "joe@joe@com"),
        Arguments.of("Joe", ""),
        Arguments.of("  ", "joe@school.com"),
        Arguments.of("", "joe@school.com"));
  }
}
