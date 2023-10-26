package com.peterczigany.profileservice.controller;

import com.peterczigany.profileservice.dto.StudentDTO;
import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentController {

  @Autowired ModelMapper mapper;

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
