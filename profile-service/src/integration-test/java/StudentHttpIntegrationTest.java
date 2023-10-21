import com.peterczigany.profileservice.ProfileServiceApplication;
import com.peterczigany.profileservice.model.Student;
import org.junit.jupiter.api.Test;
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
class StudentHttpIntegrationTest {
  private final WebTestClient testClient;

  @Autowired
  public StudentHttpIntegrationTest(WebTestClient testClient) {
    this.testClient = testClient;
  }

  @Test
  void testGet() {

    testClient
        .get()
        .uri("/students")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBodyList(Student.class);
  }

  @ParameterizedTest
  @CsvSource({"Edward Sapir,sapir@school.com,200", "Claude Lévi-Strauss,lévi@strauss@school,400"})
  void testPost(String name, String email, int responseStatus) {
    Student student = new Student(null, name, email);
    testClient
        .post()
        .uri("/students")
        .body(Mono.just(student), Student.class)
        .exchange()
        .expectStatus()
        .isEqualTo(responseStatus);
  }
}
