package am.integrations

import am.integrations.errorHandling.errorHandling
import am.integrations.features.authentication.authentication
import am.integrations.features.database.database
import am.integrations.features.logging
import am.integrations.features.serialization
import am.integrations.routing.locationRoutes
import am.integrations.routing.typeRoutes
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.locations.*
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {


    //defines how the database connection can  be performed
    database()

    // defines how data gets serialized into and from the project
    serialization()

    // defines how features.authentication should be handlend
    authentication()

    // defines how logging should be implemented
    logging()

    // defines how error logging should be implemented
    errorHandling()

    //app routing
    routing {
        typeRoutes()
        locationRoutes()
    }
}
