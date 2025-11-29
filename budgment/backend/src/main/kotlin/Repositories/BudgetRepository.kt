package com.nintech.Repositories

import com.nintech.Database.BudgetsTable
import com.nintech.Database.BudgetCategoriesTable
import com.nintech.Database.Entities.BudgetEntity
import com.nintech.Database.Models.BudgetDto
import com.nintech.dbQuery
import java.util.UUID
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.time.LocalDate

class BudgetRepository {
    suspend fun create(userId: String, name: String, amountLimit: Long, scope: String, periodType: Int, startDate: LocalDate, endDate: LocalDate, periodInterval: Int? = null, periodUnit: String? = null, customRrule: String? = null, autoCreateRecurring: Boolean = false): BudgetDto = dbQuery {
        val id = UUID.randomUUID().toString()
        val ent = BudgetEntity.new(id) {
            this.userId = userId
            this.name = name
            this.amountLimit = amountLimit
            this.scope = scope
            this.periodType = periodType
            this.periodInterval = periodInterval ?: 1
            this.periodUnit = periodUnit
            this.customRrule = customRrule
            this.startDate = startDate
            this.endDate = endDate
            this.autoCreateRecurring = if (autoCreateRecurring) 1 else 0
        }
        ent.toDto()
    }

    suspend fun findById(id: String): BudgetDto? = dbQuery {
        BudgetEntity.findById(id)?.toDto()
    }

    suspend fun findByUser(userId: String): List<BudgetDto> = dbQuery {
        BudgetEntity.find { BudgetsTable.userId eq userId }.map { it.toDto() }
    }

    suspend fun update(id: String, name: String?, amountLimit: Long?, startDate: LocalDate?, endDate: LocalDate?): BudgetDto? = dbQuery {
        val ent = BudgetEntity.findById(id) ?: return@dbQuery null
        if (name != null) ent.name = name
        if (amountLimit != null) ent.amountLimit = amountLimit
        if (startDate != null) ent.startDate = startDate
        if (endDate != null) ent.endDate = endDate
        ent.toDto()
    }

    suspend fun delete(id: String): Boolean = dbQuery {
        val ent = BudgetEntity.findById(id) ?: return@dbQuery false
        ent.delete()
        true
    }

    // Calcula gasto total para el presupuesto: suma de transacciones negativas (gastos) en el rango de fechas
    suspend fun computeSpentForBudget(budgetId: String): Long = dbQuery {
        val b = BudgetEntity.findById(budgetId) ?: throw IllegalArgumentException("Presupuesto no encontrado")
        // usar SQL directo para sumar amount donde date between startDate and endDate y category in budget_categories (si scope == 'Category')
        val tx = TransactionManager.current()
        val escaped = budgetId.replace("'", "''")
        val sql = if (b.scope == "Category") {
            // obtener categorias del budget
            "SELECT COALESCE(SUM(CASE WHEN amount < 0 THEN -amount ELSE 0 END),0) FROM transactions t WHERE t.date >= '${b.startDate}' AND t.date <= '${b.endDate}' AND t.category_id IN (SELECT category_id FROM budget_categories WHERE budget_id = '$escaped')"
        } else {
            "SELECT COALESCE(SUM(CASE WHEN amount < 0 THEN -amount ELSE 0 END),0) FROM transactions t WHERE t.date >= '${b.startDate}' AND t.date <= '${b.endDate}'"
        }
        tx.exec(sql) { rs ->
            if (rs.next()) {
                val v = rs.getLong(1)
                if (rs.wasNull()) 0L else v
            } else 0L
        } ?: 0L
    }
}
