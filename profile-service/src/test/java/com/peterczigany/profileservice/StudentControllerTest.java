package com.peterczigany.profileservice;

import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class StudentControllerTest {

  @MockBean private StudentRepository repository;

  @Autowired private StudentController subject;

  @Test
  void getAllStudents() throws Exception {

    Flux<Student> studentsFlux =
        Flux.just(
            new Student(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Joe", "joe@school.com"),
            new Student(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                "Ashley",
                "ashley@school.com"));
    Mockito.when(repository.findAll()).thenReturn(studentsFlux);

    Assertions.assertThat(subject.getAllStudents()).isEqualTo(studentsFlux);
  }

  @Test
  void addNewStudent() throws Exception {
    StudentDTO studentDTO = new StudentDTO("Joe", "joe@school.com");
    Student mappedStudent = new Student(null, "Joe", "joe@school.com");
    Student returnedStudent =
        new Student(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Joe", "joe@school.com");
    Mono<Student> returnedStudentMono = Mono.just(returnedStudent);
    Mockito.when(repository.save(mappedStudent)).thenReturn(Mono.just(returnedStudent));

    StepVerifier.create(subject.save(studentDTO))
        .expectNext(returnedStudent)
        .expectComplete()
        .verify();
  }

  @Test
  void updateStudent() {

    Student baseStudent =
        new Student(
            UUID.fromString("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3"), "Tommy", "test@school.com");
    Student updatedStudent = new Student(baseStudent);
    updatedStudent.setName("Tommy Test");
    StudentDTO request = new StudentDTO("Tommy Test", null);

    Mockito.when(repository.findById(UUID.fromString("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3")))
        .thenReturn(Mono.just(baseStudent));
    Mockito.when(repository.save(Mockito.any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

    StepVerifier.create(subject.updateStudent("1a240b6f-6535-4d59-91ca-4cf6ab4f6ca3", request))
        .expectNext(updatedStudent)
        .expectComplete()
        .verify();
  }
}
