package com.peterczigany.profileservice;

import com.peterczigany.profileservice.controller.StudentController;
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

@SpringBootTest
public class StudentControllerTest {

  @MockBean private StudentRepository repository;

  @Autowired private StudentController controller;

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

    Assertions.assertThat(controller.getAllStudents()).isEqualTo(studentsFlux);
  }
}
