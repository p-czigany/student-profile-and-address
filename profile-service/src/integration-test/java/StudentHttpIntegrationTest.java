import com.peterczigany.profileservice.ProfileServiceApplication;
import com.peterczigany.profileservice.model.Student;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
  @CsvSource({
    "Edward Sapir,sapir@school.com,200",
    "Claude Lévi-Strauss,lévi@strauss@school,400",
    ",anonymus@school.com,400"
  })
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

  @Test
  void testSuccessfulPost() {
    var returnedStudent =
        testClient
            .post()
            .uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"name\": \"Reya\", \"email\": \"reya@avernus.com\"}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert returnedStudent != null;
    Assertions.assertThat(returnedStudent.getName()).isEqualTo("Reya");
    Assertions.assertThat(returnedStudent.getEmail()).isEqualTo("reya@avernus.com");
  }

  @Test
  void testGetAfterPost() {
    testClient
        .post()
        .uri("/students")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"name\": \"Vestri\", \"email\": \"vestri@avernus.com\"}")
        .exchange()
        .expectStatus()
        .isOk();

    var returned =
        testClient
            .get()
            .uri("/students")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBodyList(Student.class)
            .returnResult()
            .getResponseBody();

    Assertions.assertThat(returned).hasAtLeastOneElementOfType(Student.class);
  }

  @Test
  void testNotHandleIdOnPost() {
    var returnedStudent =
        testClient
            .post()
            .uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                "{\"id\":\"0891583f-8d86-43bf-b6ef-941728820f0f\", \"name\": \"Reya\", \"email\": \"reya@avernus.com\"}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert returnedStudent != null;
    Assertions.assertThat(returnedStudent.getName()).isEqualTo("Reya");
    Assertions.assertThat(returnedStudent.getEmail()).isEqualTo("reya@avernus.com");
    Assertions.assertThat(returnedStudent.getId())
        .isNotEqualTo(UUID.fromString("0891583f-8d86-43bf-b6ef-941728820f0f"));
  }

  @Test
  void testPostWithSameIdShouldNotUpdate() {
    var returnedStudent =
        testClient
            .post()
            .uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"name\": \"Vestri\", \"email\": \"vestri@avernus.com\"}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert returnedStudent != null;
    var secondReturnedStudent =
        testClient
            .post()
            .uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                String.format(
                    "{\"id\": \"%s\", \"name\": \"Vestri\", \"email\": \"vestri@avernus.com\"}",
                    returnedStudent.getId()))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert secondReturnedStudent != null;
    Assertions.assertThat(secondReturnedStudent.getId()).isNotEqualTo(returnedStudent.getId());
  }

  @Test
  void testPostAndPatch() {
    var returnedStudent =
        testClient
            .post()
            .uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"name\": \"Vestri\", \"email\": \"vestri@avernus.com\"}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert returnedStudent != null;
    var returnedModifiedStudent =
        testClient
            .patch()
            .uri("/students/" + returnedStudent.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"name\": \"Vestvegr Evenwood\"}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert returnedModifiedStudent != null;
    Assertions.assertThat(returnedModifiedStudent.getId()).isEqualTo(returnedStudent.getId());
    Assertions.assertThat(returnedModifiedStudent.getName()).isEqualTo("Vestvegr Evenwood");
    Assertions.assertThat(returnedModifiedStudent.getEmail()).isEqualTo("vestri@avernus.com");
  }

  @Test
  void testFailToPatch_whenIdIsNotFound() {
    testClient
        .patch()
        .uri("/students/" + "0891583f-8d86-43bf-b6ef-941728820f0f")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"name\": \"Vestvegr Evenwood\"}")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Test
  void testFailToDelete_whenIdIsNotFound() {}

  @Test
  void testPostAndDelete() {
    var returnedStudent =
        testClient
            .post()
            .uri("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"name\": \"Vestri\", \"email\": \"vestri@avernus.com\"}")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Student.class)
            .returnResult()
            .getResponseBody();

    assert returnedStudent != null;
    var returnedModifiedStudent =
        testClient
            .delete()
            .uri("/students/" + returnedStudent.getId())
            .exchange()
            .expectStatus()
            .isNoContent();
  }
}
