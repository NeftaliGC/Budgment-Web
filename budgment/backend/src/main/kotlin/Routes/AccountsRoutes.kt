package com.nintech.Routes

import com.nintech.Database.Models.CreateAccountRequest
import com.nintech.Services.AccountService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import com.nintech.Auth.authenticatedUserId

fun Route.accountsRoutes() {
    val accountService by inject<AccountService>()

    route("/users/{userId}/accounts") {
        authenticate("auth-jwt") {
            get {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido"))
                    return@get
                }

                val authUserId = call.authenticatedUserId()
                if (authUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado"))
                    return@get
                }
                if (authUserId != userId) {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "operacion no permitida"))
                    return@get
                }

                try {
                    val list = accountService.listByUser(userId)
                    call.respond(HttpStatusCode.OK, list)
                } catch (e: Exception) {
                    call.application.environment.log.error("accounts.list failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            post {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido"))
                    return@post
                }

                val authUserId = call.authenticatedUserId()
                if (authUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado"))
                    return@post
                }
                if (authUserId != userId) {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "operacion no permitida"))
                    return@post
                }

                val req = call.receive<CreateAccountRequest>()
                try {
                    val created = accountService.create(userId, req)
                    call.respond(HttpStatusCode.Created, created)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
                } catch (e: Exception) {
                    call.application.environment.log.error("account.create failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            // NEW: obtener balance de una cuenta
            route("/{accountId}/balance") {
                get {
                    val userId = call.parameters["userId"] ?: run {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido"))
                        return@get
                    }
                    val accountId = call.parameters["accountId"] ?: run {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to "accountId requerido"))
                        return@get
                    }

                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) {
                        call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado"))
                        return@get
                    }
                    if (authUserId != userId) {
                        call.respond(HttpStatusCode.Forbidden, mapOf("error" to "operacion no permitida"))
                        return@get
                    }

                    try {
                        val balance = accountService.getWithBalance(accountId)
                        call.respond(HttpStatusCode.OK, balance)
                    } catch (e: IllegalArgumentException) {
                        // Cuenta no encontrada u otros errores de validación
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to (e.message ?: "not found")))
                    } catch (e: Exception) {
                        call.application.environment.log.error("account.balance failed", e)
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                    }
                }
            }

            // NEW: obtener balance total de todas las cuentas del usuario
            route("/total_balance") {
                get {
                    val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido")); return@get }
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@get }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@get }
                    try {
                        val total = accountService.totalBalanceForUser(userId)
                        call.respond(HttpStatusCode.OK, total)
                    } catch (e: Exception) {
                        call.application.environment.log.error("accounts.total_balance failed", e)
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                    }
                }
            }

            // NEW: obtener ingresos y gastos del último mes (todas las cuentas)
            route("/last_month") {
                route("/income") {
                    get {
                        val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido")); return@get }
                        val authUserId = call.authenticatedUserId()
                        if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@get }
                        if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@get }
                        try {
                            val res = accountService.incomeLastMonthForUser(userId)
                            call.respond(HttpStatusCode.OK, res)
                        } catch (e: Exception) {
                            call.application.environment.log.error("accounts.last_month.income failed", e)
                            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                        }
                    }
                }

                route("/expense") {
                    get {
                        val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido")); return@get }
                        val authUserId = call.authenticatedUserId()
                        if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@get }
                        if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@get }
                        try {
                            val res = accountService.expenseLastMonthForUser(userId)
                            call.respond(HttpStatusCode.OK, res)
                        } catch (e: Exception) {
                            call.application.environment.log.error("accounts.last_month.expense failed", e)
                            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                        }
                    }
                }
            }
        }
    }
}
