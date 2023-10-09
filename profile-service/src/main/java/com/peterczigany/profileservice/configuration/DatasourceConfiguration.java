package com.peterczigany.profileservice.configuration;

//import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
//import static io.r2dbc.spi.ConnectionFactoryOptions.USER;
//
//import io.netty.util.internal.StringUtil;
//import io.r2dbc.spi.ConnectionFactories;
//import io.r2dbc.spi.ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactoryOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DatasourceConfiguration {
//
//  @Bean
//  public ConnectionFactory connectionFactory(R2DBCConfigurationProperties properties) {
//
//    ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(properties.getUrl());
//    ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
//    if (!StringUtil.isNullOrEmpty(properties.getUser())) {
//      ob = ob.option(USER, properties.getUser());
//    }
//
//    if (!StringUtil.isNullOrEmpty(properties.getPassword())) {
//      ob = ob.option(PASSWORD, properties.getPassword());
//    }
//
//    return ConnectionFactories.get(ob.build());
//  }
//}
