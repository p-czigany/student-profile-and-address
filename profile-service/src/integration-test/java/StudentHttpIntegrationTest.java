import com.peterczigany.profileservice.ProfileServiceApplication;
import com.peterczigany.profileservice.model.Student;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

  @Test
  @Order(2)
  void givenStudents_whenGetStudents_thenReturnStudentList() {

    createABunchOfStudents();

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

  private void createABunchOfStudents() {
    createTestStudent("Edward Sapir", "sapir@school.com");
    createTestStudent("Annabelle", "annabelle@school.com");
    createTestStudent("Benjamin Lee Whorf", "whorf@school.com");
    createTestStudent("Anatoly Khazanov", "khazanov@school.com");
    createTestStudent("Claude Lévi-Strauss", "levi-strauss@school.com");
  }

  private void createTestStudent(String name, String email) {
    Student student = new Student(null, name, email);
    testClient.post().uri("/students").body(Mono.just(student), Student.class).exchange();
  }
}
