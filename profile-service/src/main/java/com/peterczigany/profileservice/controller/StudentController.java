package com.peterczigany.profileservice.controller;

import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import com.peterczigany.profileservice.validator.StudentPostValidator;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentController {

  private static final ModelMapper mapper = new ModelMapper();

  @Autowired private StudentRepository repository;

  public Flux<Student> getAllStudents() {
    return repository.findAll();
  }

  public Mono<Student> save(StudentDTO body) {
    return repository.save(mapper.map(body, Student.class));
  }

  public Mono<Student> updateStudent(String id, StudentDTO request) {

    return repository
        .findById(UUID.fromString(id))
        .flatMap(
            existingStudent -> {
              mapper.map(request, existingStudent);
              return repository.save(existingStudent);
            });
  }
}
