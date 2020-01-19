package am.integrations.features.errorHandling.impl

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.response.respond


inline fun < reified T: Throwable> StatusPages.Configuration.problemDetails(crossinline builder: ProblemDetails.(e: T) -> Unit) {
    exception<T> { e ->
        val pd = ProblemDetails()
            .also {
                it.type = call.request.uri
                it.details = object {}
                it.builder(e)
            }
        call.application.environment.log.error(e.toString())
        call.respond(pd.status, pd)
    }
}

class ProblemDetails {

    lateinit var title: String
    lateinit var type: String
    lateinit var status: HttpStatusCode

    var details: Any = Any()

    companion object {
        fun byBuilder(builder: ProblemDetails.() -> Unit) = ProblemDetails().apply (builder)

    }
}

