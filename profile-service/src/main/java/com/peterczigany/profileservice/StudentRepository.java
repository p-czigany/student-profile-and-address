package com.peterczigany.profileservice;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, UUID> {}
