package am.integrations.features.database.impl

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.ApplicationFeature
import io.ktor.config.HoconApplicationConfig
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalAPI
class Database() {


    var appConfig = HoconApplicationConfig(ConfigFactory.load())
    var dbUrl = appConfig.property("db.jdbcUrl").getString()
    var dbUser = appConfig.property("db.dbUser").getString()
    var dbPassword = appConfig.property("db.dbPassword").getString()
    companion object Feature : ApplicationFeature<Application, Database, Unit> {
        private var database: Database = Database()

        override val key = AttributeKey<Unit>("database")

        override fun install(pipeline: Application, configure: Database.() -> Unit) {
            database.apply(configure)
            initializeExposed()
            runMigrations()
        }

        private fun initializeExposed() {
            val config = HikariConfig()

            config.driverClassName = "org.postgresql.Driver"
            config.jdbcUrl = database.dbUrl
            config.username = database.dbUser
            config.password = database.dbPassword
            config.maximumPoolSize = 3
            config.isAutoCommit = false
            config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            config.validate()

            val hikari = HikariDataSource(config)

            org.jetbrains.exposed.sql.Database.connect(hikari)
        }

        private fun runMigrations() {
            Flyway.configure()
                .dataSource(
                    database.dbUrl,
                    database.dbUser,
                    database.dbPassword)
                .load()
                .migrate()
        }



    }
}


suspend fun <T> dbQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        transaction { block() }
    }


