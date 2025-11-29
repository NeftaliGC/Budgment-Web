package com.nintech.Database.Entities

import com.nintech.Database.BudgetsTable
import com.nintech.Database.Models.BudgetDto
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BudgetEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, BudgetEntity>(BudgetsTable)

    var userId by BudgetsTable.userId
    var name by BudgetsTable.name
    var amountLimit by BudgetsTable.amountLimit
    var scope by BudgetsTable.scope
    var periodType by BudgetsTable.periodType
    var periodInterval by BudgetsTable.periodInterval
    var periodUnit by BudgetsTable.periodUnit
    var customRrule by BudgetsTable.customRrule
    var startDate by BudgetsTable.startDate
    var endDate by BudgetsTable.endDate
    var autoCreateRecurring by BudgetsTable.autoCreateRecurring
    val createdAt by BudgetsTable.createdAt
    var updatedAt by BudgetsTable.updatedAt

    fun toDto() = BudgetDto(
        id = id.value,
        userId = userId,
        name = name,
        amountLimit = amountLimit,
        scope = scope,
        periodType = periodType,
        periodInterval = periodInterval,
        periodUnit = periodUnit,
        customRrule = customRrule,
        startDate = startDate.toString(),
        endDate = endDate.toString(),
        autoCreateRecurring = autoCreateRecurring != 0,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString()
    )
}

