package com.nintech.Routes

import com.nintech.Database.Models.CreateTransactionRequest
import com.nintech.Services.TransactionService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import com.nintech.Auth.authenticatedUserId

fun Route.transactionsRoutes() {
    val txService by inject<TransactionService>()

    route("/users/{userId}/transactions") {
        authenticate("auth-jwt") {
            get {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido"))
                    return@get
                }
                val authUserId = call.authenticatedUserId()
                if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@get }
                if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@get }
                try {
                    val list = txService.listByUser(userId)
                    call.respond(HttpStatusCode.OK, list)
                } catch (e: Exception) {
                    call.application.environment.log.error("transactions.list failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            post {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido"))
                    return@post
                }
                val authUserId = call.authenticatedUserId()
                if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@post }
                if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@post }
                val req = call.receive<CreateTransactionRequest>()
                try {
                    if (req.transferToAccountId != null) {
                        val list = txService.createTransfer(userId, req)
                        call.respond(HttpStatusCode.Created, list)
                    } else {
                        val created = txService.create(userId, req)
                        call.respond(HttpStatusCode.Created, created)
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
                } catch (e: Exception) {
                    call.application.environment.log.error("transaction.create failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            route("/{transactionId}") {
                get {
                    val txId = call.parameters["transactionId"] ?: return@get
                    val userId = call.parameters["userId"] ?: return@get
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@get }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@get }
                    val found = txService.getById(txId)
                    if (found == null) call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found")) else call.respond(HttpStatusCode.OK, found)
                }

                delete {
                    val txId = call.parameters["transactionId"] ?: return@delete
                    val userId = call.parameters["userId"] ?: return@delete
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@delete }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@delete }
                    try {
                        val ok = txService.delete(txId)
                        if (ok) call.respond(HttpStatusCode.NoContent) else call.respond(HttpStatusCode.NotFound)
                    } catch (e: Exception) {
                        call.application.environment.log.error("transaction.delete failed", e)
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                    }
                }
            }
        }
    }
}
