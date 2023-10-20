package com.peterczigany.addressservice;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AddressHttpConfiguration {

  @Bean
  RouterFunction<ServerResponse> routes(@Autowired AddressService service) {
    return route()
        .GET("/address", request -> ok().body(service.getAddress(), Address.class))
        .build();
  }
}
