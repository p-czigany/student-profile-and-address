package com.peterczigany.profileservice;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.peterczigany.profileservice.client.AddressClient;
import com.peterczigany.profileservice.dto.Address;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.spec.internal.HttpHeaders;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.cloud.contract.spec.internal.MediaTypes;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.peterczigany:address-service:+:8080",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class AddressClientTest {

  @Autowired private AddressClient client;

  @BeforeEach
  public void before() {

    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo("/address"))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.APPLICATION_JSON)
                    .withBody(
                        "{ \"id\": \"123e4567-e89b-12d3-a456-426614174111\", \"address\": \"test address\" }")));
  }

  @Test
  void getAddress() throws Exception {
    Address expectedAddress =
        new Address(UUID.fromString("123e4567-e89b-12d3-a456-426614174111"), "test address");

    Mono<Address> address = client.getAddress();
    StepVerifier.create(address).expectNext(expectedAddress).verifyComplete();
  }
}
