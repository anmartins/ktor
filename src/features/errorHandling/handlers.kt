package am.integrations.errorHandling

import am.integrations.features.errorHandling.impl.AuthenticationException
import am.integrations.features.errorHandling.impl.AuthorizationException
import am.integrations.features.errorHandling.impl.problemDetails
import com.fasterxml.jackson.databind.JsonMappingException
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode

fun Application.errorHandling() {
    install(StatusPages) {

        // Generic Unexpected 500 error
        problemDetails<AuthenticationException> { exception ->
            title = "Invalid credentials"
            status = HttpStatusCode.Unauthorized
            details = exception.toString()
        }
        // Generic Unexpected 500 error
        problemDetails<AuthorizationException> { exception ->
            title = "Insufficient permissions "
            status = HttpStatusCode.Forbidden
            details = exception.toString()
        }
        // Generic Unexpected 500 error
        problemDetails<Exception> { exception ->
            title = "Unexpected Internal Server error "
            status = HttpStatusCode.InternalServerError
            details = exception.toString()
        }

        // User supplied an invalid parameter
        problemDetails<IllegalArgumentException> { exception ->
            title = "Illegal argument supplied"
            status = HttpStatusCode.BadRequest
            details = exception.message.orEmpty()
        }

        problemDetails<JsonMappingException> { exception ->

            title = "Illegal argument supplied"
            status = HttpStatusCode.BadRequest
            details = exception.path.map { it.fieldName }
        }
    }
}