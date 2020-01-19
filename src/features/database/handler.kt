package am.integrations.features.database

import am.integrations.features.database.impl.Database
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.util.KtorExperimentalAPI
import javax.xml.crypto.Data

@KtorExperimentalAPI
fun Application.database(config: Database.() -> Unit = {}) {
    install(Database,config)

}