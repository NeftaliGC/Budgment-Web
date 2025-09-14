package com.nintech.Database.Entities

import com.nintech.Database.Models.UserDto
import com.nintech.Database.UsersTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<String>): Entity<String>(id) {
    companion object: EntityClass<String, UserEntity>(UsersTable)

    var name by UsersTable.name
    var username by UsersTable.username
    var passwordHash by UsersTable.passwordHash
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt

    fun toDto() = UserDto(
        id = id.value,
        name = name,
        username = username,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString()
    )

}