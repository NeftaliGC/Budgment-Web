package com.nintech.Services

import com.nintech.Database.Models.CategoryDto
import com.nintech.Database.Models.CreateCategoryRequest
import com.nintech.Repositories.CategoryRepository

class CategoryService(private val repo: CategoryRepository) {
    suspend fun listByUser(userId: String): List<CategoryDto> = repo.findByUser(userId)

    suspend fun getById(id: String): CategoryDto? = repo.findById(id)

    suspend fun create(userId: String, req: CreateCategoryRequest): CategoryDto {
        require(req.name.isNotBlank()) { "Nombre vacío" }
        require(req.type == "Gasto" || req.type == "Ingreso") { "Tipo inválido" }
        return repo.create(userId, req.name, req.type)
    }

    suspend fun update(id: String, req: com.nintech.Database.Models.UpdateCategoryRequest): CategoryDto? {
        return repo.update(id, req.name, req.type)
    }

    suspend fun delete(id: String): Boolean = repo.softDelete(id)
}

