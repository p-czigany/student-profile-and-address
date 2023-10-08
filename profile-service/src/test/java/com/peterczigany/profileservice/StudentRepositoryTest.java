package com.peterczigany.profileservice;

import io.r2dbc.h2.H2ConnectionFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;


@SpringBootTest(classes = R2DBCConfiguration.class)
class StudentRepositoryTest {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private DatabaseClient dbClient;

  @Autowired
  private H2ConnectionFactory h2ConnectionFactory;

  @BeforeEach
  public void setup() {

    Hooks.onOperatorDebug();

    List<String> statements = Arrays.asList(
        "DROP TABLE IF EXISTS student;",
        "CREATE table student (id UUID, name VARCHAR2, email VARCHAR2);");

    statements.forEach(it -> dbClient.sql(it)
        .fetch()
        .rowsUpdated()
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete());
  }

  @Test
  void whenDeleteAll_then0IsExpected() {
    studentRepository.deleteAll()
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void whenInsert2_then6AreExpected() {
    insertStudents();
    studentRepository.findAll()
        .as(StepVerifier::create)
        .expectNextCount(2)
        .verifyComplete();
  }

  private void insertStudents() {
    List<Student> players = Arrays.asList(
        new Student(null, "Joe",
            "joe@school.com"),
        new Student(null, "Ashley",
            "ashley@school.com")
    );

    studentRepository.saveAll(players).subscribe();
  }

}
