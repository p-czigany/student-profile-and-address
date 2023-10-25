package com.peterczigany.profileservice.configuration;

import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.validator.StudentPatchValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StudentPatchValidationHandler
    extends AbstractValidationHandler<StudentDTO, StudentPatchValidator> {

  @Autowired private StudentController controller;

  private StudentPatchValidationHandler() {
    super(StudentDTO.class, new StudentPatchValidator());
  }

  @Override
  protected Mono<ServerResponse> processBody(StudentDTO validBody, ServerRequest originalRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            controller.updateStudent(originalRequest.pathVariable("id"), validBody), Student.class);
  }
}
