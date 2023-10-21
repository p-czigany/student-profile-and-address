import com.peterczigany.profileservice.ProfileServiceApplication;
import com.peterczigany.profileservice.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = ProfileServiceApplication.class)
@AutoConfigureWebTestClient
@TestMethodOrder(OrderAnnotation.class)
class StudentHttpIntegrationTest {
  private final WebTestClient testClient;

  @Autowired
  public StudentHttpIntegrationTest(WebTestClient testClient) {
    this.testClient = testClient;
  }

  @Test
  @Order(1)
  void givenNoStudents_whenGetStudents_thenReturnWithEmptyList() {

    testClient
        .get()
        .uri("/students")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBodyList(Student.class)
        .hasSize(0);
  }

  @ParameterizedTest
  @CsvSource({
    "Edward Sapir,sapir@school.com",
    "Annabelle,annabelle@school.com",
    "Benjamin Lee Whorf,whorf@school.com",
    "Anatoly Khazanov,khazanov@school.com",
    "Claude Lévi-Strauss,levi-strauss@school.com"
  })
  @Order(2)
  void shouldReturnSameNameAndEmail_whenStudentIsPosted(String name, String email) {
    Student student = new Student(null, name, email);
    Student response =
        testClient
            .post()
            .uri("/students")
            .body(Mono.just(student), Student.class)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();
    assert response != null;
    Assertions.assertThat(response.getName()).isEqualTo(student.getName());
    Assertions.assertThat(response.getEmail()).isEqualTo(student.getEmail());
  }

  @Test
  @Order(3)
  void givenStudents_whenGetStudents_thenReturnStudentList() {

    testClient
        .get()
        .uri("/students")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBodyList(Student.class)
        .hasSize(5);
  }
}
