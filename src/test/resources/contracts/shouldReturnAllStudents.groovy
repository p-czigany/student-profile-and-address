package contracts

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

Contract.make {
    description("Should return all students")
    request {
        url("/students")
        method(HttpMethods.GET)
    }
    response {
        status(HttpStatus.OK.value())
        body([
                [id: UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), name: "Joe", email: "joe@school.com"],
                [id: UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), name: "Ashley", email: "ashley@school.com"]
        ])
        headers {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        }
    }
}
