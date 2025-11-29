package com.nintech.Services

import com.nintech.Database.Models.BudgetDto
import com.nintech.Repositories.BudgetRepository
import java.time.LocalDate

class BudgetService(private val repo: BudgetRepository) {
    suspend fun create(userId: String, name: String, amountLimit: Long, scope: String, periodType: Int, startDate: LocalDate, endDate: LocalDate): BudgetDto {
        // validaciones básicas
        if (name.isBlank()) throw IllegalArgumentException("name requerido")
        if (amountLimit < 0) throw IllegalArgumentException("amountLimit no puede ser negativo")
        if (startDate.isAfter(endDate)) throw IllegalArgumentException("startDate debe ser <= endDate")
        return repo.create(userId, name, amountLimit, scope, periodType, startDate, endDate)
    }

    suspend fun getById(id: String): BudgetDto? = repo.findById(id)
    suspend fun listByUser(userId: String) = repo.findByUser(userId)
    suspend fun delete(id: String) = repo.delete(id)
    suspend fun update(id: String, name: String?, amountLimit: Long?, startDate: LocalDate?, endDate: LocalDate?) = repo.update(id, name, amountLimit, startDate, endDate)
    suspend fun spentForBudget(budgetId: String) = repo.computeSpentForBudget(budgetId)
}

