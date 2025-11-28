package com.nintech.Repositories

import com.nintech.Database.CategoriesTable
import com.nintech.Database.Entities.CategoryEntity
import com.nintech.dbQuery
import java.time.LocalDateTime
import java.util.UUID

class CategoryRepository {
    suspend fun findByUser(userId: String) = dbQuery {
        CategoryEntity.find { CategoriesTable.userId eq userId }.map { it.toDto() }
    }

    suspend fun findById(id: String) = dbQuery {
        CategoryEntity.findById(id)?.toDto()
    }

    suspend fun create(userId: String, name: String, type: String) = dbQuery {
        val id = UUID.randomUUID().toString()
        val ent = CategoryEntity.new(id) {
            this.userId = userId
            this.name = name
            this.type = type
        }
        ent.toDto()
    }

    suspend fun update(id: String, name: String?, type: String?) = dbQuery {
        val ent = CategoryEntity.findById(id) ?: return@dbQuery null
        if (name != null) ent.name = name
        if (type != null) ent.type = type
        ent.toDto()
    }

    suspend fun softDelete(id: String) = dbQuery {
        val ent = CategoryEntity.findById(id) ?: return@dbQuery false
        ent.deletedAt = LocalDateTime.now()
        true
    }
}
