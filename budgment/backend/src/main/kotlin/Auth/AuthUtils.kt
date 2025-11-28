package com.nintech.Auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal

fun ApplicationCall.authenticatedUserId(): String? {
    val principal = this.principal<JWTPrincipal>() ?: return null
    return principal.payload.getClaim("userId").asString()
}

