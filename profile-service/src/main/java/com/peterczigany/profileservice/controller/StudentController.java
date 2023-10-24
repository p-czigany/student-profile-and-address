package com.peterczigany.profileservice.controller;

import com.peterczigany.profileservice.model.Student;
import com.peterczigany.profileservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StudentController {

  @Autowired private StudentRepository repository;

  public Flux<Student> getAllStudents() {
    return repository.findAll();
  }
}
