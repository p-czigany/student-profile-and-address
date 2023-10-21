package com.peterczigany.profileservice.validator;

import com.peterczigany.profileservice.model.Student;
import java.util.regex.Pattern;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class StudentValidator implements Validator {

  private static final String EMAIL = "email";

  @Override
  public boolean supports(Class<?> clazz) {
    return Student.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL, "field.required");
    Student student = (Student) target;
    if (student.getEmail() != null
        && !Pattern.compile(
                "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
            .matcher(student.getEmail())
            .matches()) {
      errors.rejectValue(
          EMAIL, "field.format", new Object[] {EMAIL}, "The email must be a valid email address.");
    }
  }
}
