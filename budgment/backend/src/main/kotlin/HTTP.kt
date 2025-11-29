package com.nintech

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.slf4j.event.*

fun Application.configureHTTP() {
    install(CORS) {
        allowCredentials = true // <- MUY IMPORTANTE

        // Tu frontend (Next.js)
        allowHost("localhost:3000", schemes = listOf("http"))
        allowHost("api.budgment.nintech.engineer", schemes = listOf("http"))

        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)   // <- NECESARIO
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        allowHeader(HttpHeaders.ContentType)  // <- NECESARIO para JSON
        allowHeader(HttpHeaders.Authorization)
    }

}
