package com.peterczigany.profileservice.repository;

import com.peterczigany.profileservice.model.Student;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StudentRepository extends ReactiveCrudRepository<Student, UUID> {}
