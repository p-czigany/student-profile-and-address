package com.peterczigany.profileservice;

import java.util.Arrays;
import java.util.List;

import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@DataR2dbcTest
class StudentEntityTest {

  @Autowired private StudentRepository repository;

  @Autowired private DatabaseClient dbClient;

  @BeforeEach
  public void setup() {

    Hooks.onOperatorDebug();

    List<String> statements =
        Arrays.asList(
            "DROP TABLE IF EXISTS student;",
            "CREATE table student (id UUID DEFAULT UUID(), name VARCHAR2, email VARCHAR2);");

    statements.forEach(
        it ->
            dbClient
                .sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
  }

  @Test
  void persist() throws Exception {

    Flux<Student> students =
        Flux.just(
            new Student(null, "Joe", "joe@school.com"),
            new Student(null, "Ashley", "ashley@school.com"));
    Flux<Student> retrievedStudents = repository.saveAll(students);

    StepVerifier.create(retrievedStudents)
        .expectNextCount(1)
        .expectNextMatches(student -> student.getName().equals("Ashley"))
        .verifyComplete();
  }

  @Test
  void whenDeleteAll_then0IsExpected() {
    repository.deleteAll().as(StepVerifier::create).expectNextCount(0).verifyComplete();
  }

  @Test
  void whenInsert2_then2AreExpected() {
    insertStudents();
    repository.findAll().as(StepVerifier::create).expectNextCount(2).verifyComplete();
  }

  private void insertStudents() {
    List<Student> students =
        Arrays.asList(
            new Student(null, "Joe", "joe@school.com"),
            new Student(null, "Ashley", "ashley@school.com"));

    repository.saveAll(students).subscribe();
  }
}
