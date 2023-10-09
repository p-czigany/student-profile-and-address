package com.peterczigany.profileservice;

import com.peterczigany.profileservice.configuration.StudentHttpConfiguration;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import io.restassured.RestAssured;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;

@Import(StudentHttpConfiguration.class)
@SpringBootTest(
    properties = "server.port=0",
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

  @MockBean private StudentRepository repository;

  @LocalServerPort private int port;

  @BeforeEach
  public void before() throws Exception {
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

    RestAssured.baseURI = "http://localhost:" + port;
  }
}
