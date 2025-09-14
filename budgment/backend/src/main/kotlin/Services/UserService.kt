package com.nintech.Services

import com.nintech.Database.Models.CreateUserRequest
import com.nintech.Database.Models.UserDto
import com.nintech.Repositories.UserRepository
import com.nintech.Utils.*

class UserService(private val repo: UserRepository) {
    suspend fun register(createReq: CreateUserRequest): UserDto {
        require(createReq.name.isNotBlank()) {"Nombre vacío"}
        require(createReq.username.isNotBlank()) {"Nombre de usuario vacío"}
        require(createReq.password.length >= 8) {"Contraseña debe tener minimo 8 caracteres"}

        val existing = repo.findByUsername(createReq.username)
        if (existing != null) throw IllegalArgumentException("El usuario ya existe")

        val hashed = hashPassword(createReq.password)

        val userDto = repo.create(createReq.name, createReq.username, hashed)
        return userDto
    }

    suspend fun authenticate(username: String, password: String): UserDto? {
        val entity = repo.findEntityByUsername(username) ?: return null
        return if (verifyPassword(password, entity.passwordHash)) entity.toDto() else null
    }
}