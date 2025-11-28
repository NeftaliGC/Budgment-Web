package com.nintech.Database.Entities

import com.nintech.Database.CategoriesTable
import com.nintech.Database.Models.CategoryDto
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CategoryEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CategoryEntity>(CategoriesTable)

    var userId by CategoriesTable.userId
    var name by CategoriesTable.name
    var type by CategoriesTable.type
    var createdAt by CategoriesTable.createdAt
    var updatedAt by CategoriesTable.updatedAt
    var deletedAt by CategoriesTable.deletedAt

    fun toDto() = CategoryDto(
        id = id.value,
        userId = userId,
        name = name,
        type = type,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString(),
        deletedAt = deletedAt?.toString()
    )
}

