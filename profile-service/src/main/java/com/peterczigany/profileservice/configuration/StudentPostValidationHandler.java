package com.peterczigany.profileservice.configuration;

import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.validator.StudentPostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StudentPostValidationHandler
    extends AbstractValidationHandler<StudentDTO, StudentPostValidator> {

  @Autowired private StudentController controller;

  private StudentPostValidationHandler() {
    super(StudentDTO.class, new StudentPostValidator());
  }

  @Override
  protected Mono<ServerResponse> processBody(StudentDTO validBody, ServerRequest originalRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(controller.save(validBody), Student.class);
  }
}
