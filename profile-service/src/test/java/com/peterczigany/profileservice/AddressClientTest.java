package com.peterczigany.profileservice;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.spec.internal.HttpHeaders;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.cloud.contract.spec.internal.MediaTypes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
// @AutoConfigureStubRunner(
//        ids = "com.peterczigany:address-service:+:8080",
//        stubsMode = StubRunnerProperties.StubsMode.LOCAL
// )
@WireMockTest(httpPort = 8080)
@ExtendWith(SpringExtension.class)
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
                        "{ \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"address\": \"address placeholder\" }")));
  }

  @Test
  void getAddress() throws Exception {
    Address expectedAddress =
        new Address(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "address placeholder");

    Mono<Address> address = client.getAddress();
    StepVerifier.create(address).expectNext(expectedAddress).verifyComplete();
  }
}
