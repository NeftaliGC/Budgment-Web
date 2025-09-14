package com.nintech.Repositories

import com.nintech.Database.Entities.UserEntity
import com.nintech.Database.Models.UserDto
import com.nintech.Database.UsersTable
import com.nintech.dbQuery
import java.util.UUID

class UserRepository {
    suspend fun findByUsername(username: String): UserDto? = dbQuery {
        UserEntity.find { UsersTable.username eq username }.singleOrNull()?.toDto()
    }

    suspend fun findEntityByUsername(username: String): UserEntity? = dbQuery {
        UserEntity.find { UsersTable.username eq username }.singleOrNull()
    }

    suspend fun create(name: String, username: String, passwordHash: String): UserDto = dbQuery {
        val id = UUID.randomUUID().toString()
        val ent = UserEntity.new(id) {
            this.name = name
            this.username = username
            this.passwordHash = passwordHash
        }
        ent.toDto()
    }
}