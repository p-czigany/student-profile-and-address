package com.peterczigany.profileservice.configuration;

import com.peterczigany.profileservice.controller.StudentController;
import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.validator.StudentPatchValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
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
    return controller
        .updateStudent(originalRequest.pathVariable("id"), validBody);
//        .switchIfEmpty(
//            Mono.error(
//                new ResponseStatusException(
//                    HttpStatus.UNPROCESSABLE_ENTITY, "No student is found with the given id.")))
//        .flatMap(
//            student ->
//                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(student))
//        .onErrorResume(
//            ResponseStatusException.class,
//            ex ->
//                ServerResponse.status(ex.getStatusCode())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(ex.getReason()));
  }
}
