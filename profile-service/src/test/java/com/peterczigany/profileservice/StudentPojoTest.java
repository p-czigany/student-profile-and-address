package com.peterczigany.profileservice;

import java.util.UUID;

import com.peterczigany.profileservice.model.Student;
import org.assertj.core.api.Assertions;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class StudentPojoTest {

  @Test
  void create() throws Exception {

    Student student =
        new Student(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            "Joe Manganiello",
            "joemanganiello@school.com");
    Assertions.assertThat(student.getName()).isEqualTo("Joe Manganiello");
    MatcherAssert.assertThat("joe manganiello", Matchers.equalToIgnoringCase("joe manganiello"));
    MatcherAssert.assertThat(
        student.getName(),
        new BaseMatcher<>() {

          @Override
          public void describeTo(Description description) {
            description.appendText("the name should be valid uppercase!");
          }

          @Override
          public boolean matches(Object actual) {
            return Character.isUpperCase(((String) actual).charAt(0));
          }
        });
    Assertions.assertThat(student.getName()).isEqualToIgnoringCase("joe manganiello");
  }
}
