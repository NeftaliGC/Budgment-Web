package com.nintech.Routes

import com.nintech.Database.Models.BudgetCategoryDto
import com.nintech.Database.Models.BudgetDto as ModelBudgetDto
import com.nintech.Services.BudgetService
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
import com.nintech.Database.Models.CreateCategoryRequest
import com.nintech.Database.Models.BudgetDto
import com.nintech.Database.Models.CreateAccountRequest
import java.time.LocalDate

// DTO para petición de creación simplificada
@kotlinx.serialization.Serializable
data class CreateBudgetRequest(
    val name: String,
    val amountLimit: Long,
    val scope: String,
    val periodType: Int,
    val startDate: String,
    val endDate: String
)

fun Route.budgetsRoutes() {
    val budgetService by inject<BudgetService>()

    route("/users/{userId}/budgets") {
        authenticate("auth-jwt") {
            get {
                val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido")); return@get }
                val authUserId = call.authenticatedUserId()
                if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado")); return@get }
                if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden, mapOf("error" to "operacion no permitida")); return@get }
                try {
                    val list = budgetService.listByUser(userId)
                    call.respond(HttpStatusCode.OK, list)
                } catch (e: Exception) {
                    call.application.environment.log.error("budgets.list failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            post {
                val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido")); return@post }
                val authUserId = call.authenticatedUserId()
                if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado")); return@post }
                if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden, mapOf("error" to "operacion no permitida")); return@post }

                val req = call.receive<CreateBudgetRequest>()
                try {
                    val start = LocalDate.parse(req.startDate)
                    val end = LocalDate.parse(req.endDate)
                    val created = budgetService.create(userId, req.name, req.amountLimit, req.scope, req.periodType, start, end)
                    call.respond(HttpStatusCode.Created, created)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
                } catch (e: Exception) {
                    call.application.environment.log.error("budget.create failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            // obtener un presupuesto por id
            route("/{budgetId}") {
                get {
                    val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "userId requerido")); return@get }
                    val budgetId = call.parameters["budgetId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "budgetId requerido")); return@get }
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado")); return@get }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden, mapOf("error" to "operacion no permitida")); return@get }
                    try {
                        val b = budgetService.getById(budgetId) ?: run { call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found")); return@get }
                        call.respond(HttpStatusCode.OK, b)
                    } catch (e: Exception) {
                        call.application.environment.log.error("budget.get failed", e)
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                    }
                }

                // obtener gasto registrado para el presupuesto
                route("/spent") {
                    get {
                        val budgetId = call.parameters["budgetId"] ?: run { call.respond(HttpStatusCode.BadRequest, mapOf("error" to "budgetId requerido")); return@get }
                        val authUserId = call.authenticatedUserId() ?: run { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "no autenticado")); return@get }
                        try {
                            val spent = budgetService.spentForBudget(budgetId)
                            call.respond(HttpStatusCode.OK, mapOf("spentMinorUnits" to spent, "spent" to (kotlin.math.abs(spent))))
                        } catch (e: Exception) {
                            call.application.environment.log.error("budget.spent failed", e)
                            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                        }
                    }
                }

            }

        }
    }
}

