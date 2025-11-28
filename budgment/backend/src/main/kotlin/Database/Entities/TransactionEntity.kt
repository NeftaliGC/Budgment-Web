package com.nintech.Database.Entities

import com.nintech.Database.TransactionsTable
import com.nintech.Database.Models.TransactionDto
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate

class TransactionEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TransactionEntity>(TransactionsTable)

    var userId by TransactionsTable.userId
    var accountId by TransactionsTable.accountId
    var categoryId by TransactionsTable.categoryId
    var amount by TransactionsTable.amount
    var currency by TransactionsTable.currency
    var exchangeRateAtTransaction by TransactionsTable.exchangeRateAtTransaction
    var description by TransactionsTable.description
    var date by TransactionsTable.date
    var createdAt by TransactionsTable.createdAt
    var updatedAt by TransactionsTable.updatedAt
    var transferId by TransactionsTable.transferId
    var recurringId by TransactionsTable.recurringId

    fun toDto() = TransactionDto(
        id = id.value,
        userId = userId,
        accountId = accountId,
        categoryId = categoryId,
        amount = amount,
        currency = currency,
        exchangeRateAtTransaction = exchangeRateAtTransaction,
        description = description,
        date = date.toString(),
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString(),
        transferId = transferId,
        recurringId = recurringId
    )
}

