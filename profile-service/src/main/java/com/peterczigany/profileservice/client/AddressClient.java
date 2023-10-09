package com.peterczigany.profileservice.client;

import com.peterczigany.profileservice.dto.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AddressClient {

  private final WebClient webClient;

  public Mono<Address> getAddress() {
    return webClient
        .get()
        .uri("http://localhost:8080/address")
        .retrieve()
        .bodyToMono(Address.class);
  }
}
