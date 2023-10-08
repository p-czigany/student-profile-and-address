package com.peterczigany.profileservice;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Address {

  private UUID id;
  private String schoolAddress;

}
