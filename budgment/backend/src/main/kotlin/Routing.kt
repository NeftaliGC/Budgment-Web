package com.nintech

import com.nintech.Routes.userRoutes
import com.nintech.Routes.accountsRoutes
import com.nintech.Routes.categoriesRoutes
import com.nintech.Routes.transactionsRoutes
import com.nintech.Services.UserService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/") {
            val status = mapOf("name" to "Budgment API", "version" to "0.0.1", "status" to "active")
            call.respond(HttpStatusCode.OK, status)
        }
        userRoutes()
        accountsRoutes()
        categoriesRoutes()
        transactionsRoutes()
    }

}
