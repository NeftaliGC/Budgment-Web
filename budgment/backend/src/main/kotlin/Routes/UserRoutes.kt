package com.nintech.Routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nintech.Auth.RefreshSession
import com.nintech.Auth.UserSession
import com.nintech.Database.Models.CreateUserRequest
import com.nintech.Database.Models.UserLogin
import com.nintech.Services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.koin.core.qualifier.named
import java.lang.Exception
import org.koin.ktor.ext.inject
import java.util.Date

fun Route.userRoutes() {
    val userService by inject<UserService>()
    val secret: String by inject(named("secret"))
    val issuer: String by inject(named("issuer"))
    val audience: String by inject(named("audience"))

    route("/users") {

        post("/sign_in") {
            val req = call.receive<CreateUserRequest>()
            try {
                val created = userService.register(req)
                call.respond(HttpStatusCode.Created, created)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
            } catch (e: Exception) {
                call.application.environment.log.error("user.created failed", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
            }
        }

        post("/login") {
            val req = call.receive<UserLogin>()

            try {
                val user = userService.authenticate(req.username, req.password)

                if (user != null) {

                    val accessToken = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("username", user.username)
                        .withClaim("userId", user.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .sign(Algorithm.HMAC256(secret))

                    val refreshToken = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("username", user.username)
                        .withClaim("userId", user.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                        .sign(Algorithm.HMAC256(secret))

                    call.sessions.set(UserSession(accessToken))
                    call.sessions.set(RefreshSession(refreshToken))

                    // devolver tokens también en el body para uso por API, ahora incluyendo userId
                    call.respond(HttpStatusCode.OK, mapOf("accessToken" to accessToken, "refreshToken" to refreshToken, "userId" to user.id))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to ("Usuario o Contraseña invalidos")))
                }

            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
            } catch (e: Exception) {
                call.application.environment.log.error("user.login failed", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
            }

        }

        authenticate("auth-jwt") {
            get("/refresh") {
                val refreshSession = call.sessions.get<RefreshSession>()
                val secret: String by inject(named("secret"))
                val issuer: String by inject(named("issuer"))
                val audience: String by inject(named("audience"))

                if (refreshSession == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No hay refresh token"))
                    return@get
                }
                val token = refreshSession.token
                try {
                    val decoded = JWT.require(Algorithm.HMAC256(secret))
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .build()
                        .verify(token)

                    val username = decoded.getClaim("username").asString()
                    val userId = decoded.getClaim("userId").asString()

                    val newAccessToken = JWT.create()
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .withClaim("username", username)
                        .withClaim("userId", userId)
                        .withExpiresAt(Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .sign(Algorithm.HMAC256(secret))

                    val newRefreshToken = JWT.create()
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .withClaim("username", username)
                        .withClaim("userId", userId)
                        .withExpiresAt(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                        .sign(Algorithm.HMAC256(secret))

                    call.sessions.set(UserSession(newAccessToken))
                    call.sessions.set(RefreshSession(newRefreshToken))
                    call.respond(HttpStatusCode.OK, mapOf("accessToken" to newAccessToken, "refreshToken" to newRefreshToken, "userId" to userId))
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
                } catch (e: Exception) {
                    call.application.environment.log.error("user.login failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }
        }

    }
}