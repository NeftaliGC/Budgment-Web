package com.nintech.Auth

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import kotlinx.serialization.Serializable
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject


fun Application.authConfig() {
    val secret: String by inject(named("secret"))
    val issuer: String by inject(named("issuer"))
    val audience: String by inject(named("audience"))


    install(Sessions) {
        cookie<UserSession>("ACCESS_TOKEN") {
            cookie.httpOnly = true
            cookie.path = "/"
            cookie.maxAgeInSeconds = 15 * 60
            cookie.secure = this@authConfig
                .environment.config
                .propertyOrNull("ktor.deployment.ssl")
                ?.getString()
                ?.toBoolean()?: false
        }

        cookie<RefreshSession>("REFRESH_TOKEN") {
            cookie.path = "/users/refresh"
            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60
            cookie.httpOnly = true
            cookie.secure = this@authConfig
                .environment
                .config
                .propertyOrNull("ktor.deployment.ssl")
                ?.getString()
                ?.toBoolean() ?: false
        }
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )
            validate { credential ->
                if(credential.payload.getClaim("username").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}

@Serializable
data class UserSession(val token: String)
@Serializable
data class RefreshSession(val token: String)
