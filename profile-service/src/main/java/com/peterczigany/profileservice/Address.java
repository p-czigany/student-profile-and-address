package com.peterczigany.profileservice;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {

  private UUID id;
//  private String id;
  private String address;

}
