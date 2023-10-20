package com.peterczigany.addressservice;

import io.restassured.RestAssured;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

@Import(AddressHttpConfiguration.class)
@SpringBootTest(
    properties = "server.port=0",
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseClass {

  @MockBean private AddressService service;

  @LocalServerPort private int port;

  @BeforeEach
  public void before() throws Exception {
    Mockito.when(service.getAddress())
        .thenReturn(
            Mono.just(
                new Address(
                    UUID.fromString("123e4567-e89b-12d3-a456-426614174010"),
                    "address placeholder")));

    RestAssured.baseURI = "http://localhost:" + port;
  }
}
