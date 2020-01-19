package am.integrations.features.authentication

import am.integrations.features.authentication.impl.JwtConfig
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt

fun Application.authentication() {

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate {
                JWTPrincipal(it.payload)
            }
        }
    }
}

