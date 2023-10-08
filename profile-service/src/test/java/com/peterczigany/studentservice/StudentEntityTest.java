package com.peterczigany.studentservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class StudentEntityTest {

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void persist() throws Exception {

    Student student = new Student(null, "Joe", "joe@school.com");
    Student retrievedStudent = this.entityManager.persistFlushFind(student);

    Assertions.assertThat(retrievedStudent.getName()).isEqualTo("Joe");
    Assertions.assertThat(retrievedStudent.getId()).isNotNull();
  }
}
