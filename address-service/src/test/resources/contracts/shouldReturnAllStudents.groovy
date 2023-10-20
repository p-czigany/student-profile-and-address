package contracts

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

Contract.make {
    description("Should return all addresses")
    request {
        url("/address")
        method(HttpMethods.GET)
    }
    response {
        status(HttpStatus.OK.value())
        body(
            id: UUID.fromString("123e4567-e89b-12d3-a456-426614174010"), address: "address placeholder"
        )
        headers {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        }
    }
}
