package com.peterczigany.profileservice;


import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

@AutoConfigureMockMvc
@Import(StudentHttpConfiguration.class)
@SpringBootTest(
    properties = "server.port=0",
    webEnvironment = WebEnvironment.RANDOM_PORT
)
public class BaseClass {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentRepository repository;

  @LocalServerPort
  private int port;

  @BeforeEach
  public void before() throws Exception {
    Mockito.when(this.repository.findAll()).thenReturn(
        Flux.just(
            new Student(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Joe",
                "joe@school.com"),
            new Student(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), "Ashley",
                "ashley@school.com")
        ));

    RestAssured.baseURI = "http://localhost:" + this.port;
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

}
