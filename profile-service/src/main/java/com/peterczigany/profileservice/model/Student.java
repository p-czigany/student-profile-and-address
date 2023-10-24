package com.peterczigany.profileservice.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Student {

  @Id private UUID id;
  private String name;
  private String email;

  public Student(Student student) {
    this(student.getId(), student.getName(), student.getEmail());
  }
}
