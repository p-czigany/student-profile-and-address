package com.peterczigany.addressservice;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddressService {
  public Mono<Address> getAddress() {
    return Mono.just(new Address(UUID.fromString("123e4567-e89b-12d3-a456-426614174010"), "address placeholder"));
  }
}
