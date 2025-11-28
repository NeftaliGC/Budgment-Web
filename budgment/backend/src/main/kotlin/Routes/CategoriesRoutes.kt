package com.nintech.Routes

import com.nintech.Database.Models.CreateCategoryRequest
import com.nintech.Database.Models.UpdateCategoryRequest
import com.nintech.Services.CategoryService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import com.nintech.Auth.authenticatedUserId

fun Route.categoriesRoutes() {
    val categoryService by inject<CategoryService>()

    route("/users/{userId}/categories") {
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
                    val list = categoryService.listByUser(userId)
                    call.respond(HttpStatusCode.OK, list)
                } catch (e: Exception) {
                    call.application.environment.log.error("categories.list failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            post {
                val userId = call.parameters["userId"] ?: run { call.respond(HttpStatusCode.BadRequest); return@post }
                val authUserId = call.authenticatedUserId()
                if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@post }
                if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@post }
                val req = call.receive<CreateCategoryRequest>()
                try {
                    val created = categoryService.create(userId, req)
                    call.respond(HttpStatusCode.Created, created)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
                } catch (e: Exception) {
                    call.application.environment.log.error("category.create failed", e)
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                }
            }

            route("/{categoryId}") {
                get {
                    val catId = call.parameters["categoryId"] ?: return@get
                    val userId = call.parameters["userId"] ?: return@get
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@get }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@get }
                    val found = categoryService.getById(catId)
                    if (found == null) {
                        call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
                    } else {
                        call.respond(HttpStatusCode.OK, found)
                    }
                }

                patch {
                    val catId = call.parameters["categoryId"] ?: return@patch
                    val userId = call.parameters["userId"] ?: return@patch
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@patch }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@patch }
                    val req = call.receive<UpdateCategoryRequest>()
                    try {
                        val updated = categoryService.update(catId, req)
                        if (updated == null) {
                            call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
                        } else {
                            call.respond(HttpStatusCode.OK, updated)
                        }
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "invalid")))
                    } catch (e: Exception) {
                        call.application.environment.log.error("category.update failed", e)
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                    }
                }

                delete {
                    val catId = call.parameters["categoryId"] ?: return@delete
                    val userId = call.parameters["userId"] ?: return@delete
                    val authUserId = call.authenticatedUserId()
                    if (authUserId == null) { call.respond(HttpStatusCode.Unauthorized); return@delete }
                    if (authUserId != userId) { call.respond(HttpStatusCode.Forbidden); return@delete }
                    try {
                        val ok = categoryService.delete(catId)
                        if (ok) call.respond(HttpStatusCode.NoContent) else call.respond(HttpStatusCode.NotFound)
                    } catch (e: Exception) {
                        call.application.environment.log.error("category.delete failed", e)
                        call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal"))
                    }
                }
            }
        }
    }
}
