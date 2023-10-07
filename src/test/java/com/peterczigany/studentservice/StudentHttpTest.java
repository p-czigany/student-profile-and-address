package com.peterczigany.studentservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(StudentHttpConfiguration.class)
class StudentHttpTest {

  @MockBean
  private StudentRepository repository;

  @Autowired
  private MockMvc client;

  @Test
  void getAllStudents() throws Exception {

    Mockito.when(this.repository.findAll()).thenReturn(
        List.of(
            new Student(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Joe",
                "joe@school.com"),
            new Student(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), "Ashley",
                "ashley@school.com")
        ));

    this.client.perform(MockMvcRequestBuilders.get(
            "/students"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("@.[0].name").value("Joe"))
        .andExpect(jsonPath("@.[1].name").value("Ashley"));
  }
}
