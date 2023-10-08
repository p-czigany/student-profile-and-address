package com.peterczigany.profileservice;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AddressClientTest {

  @Autowired
  private AddressClient client;

  @Test
  void getAddress() throws Exception {
    Address expectedAddress = new Address(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        "school address placeholder");

    Assertions.assertEquals(expectedAddress, client.getAddress());
  }

}
