package com.peterczigany.profileservice;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, UUID> {

}
