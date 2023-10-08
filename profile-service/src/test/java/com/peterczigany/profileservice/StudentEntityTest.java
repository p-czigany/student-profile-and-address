package com.peterczigany.profileservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataR2dbcTest
class StudentEntityTest {

  @Autowired
  private StudentRepository repository;

  @Test
  void persist() throws Exception {

    Flux<Student> students = Flux.just(new Student(null, "Joe", "joe@school.com"),
        new Student(null, "Ashley", "ashley@school.com"));
    Flux<Student> retrievedStudents = this.repository.saveAll(students);

    StepVerifier
        .create(retrievedStudents)
        .expectNextCount(1)
        .expectNextMatches(student -> student.getName().equals("Joe"))
//        .expectNextMatches(student -> student.getName().equals("Ashley"))
        .verifyComplete();
//    Assertions.assertThat(retrievedStudent.getName()).isEqualTo("Joe");
//    Assertions.assertThat(retrievedStudent.getId()).isNotNull();
  }

}
