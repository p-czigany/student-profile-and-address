package com.peterczigany.profileservice.repository;

import java.util.UUID;

import com.peterczigany.profileservice.model.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StudentRepository extends ReactiveCrudRepository<Student, UUID> {}
