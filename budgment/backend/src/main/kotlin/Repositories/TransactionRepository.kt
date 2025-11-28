package com.nintech.Repositories

import com.nintech.Database.TransactionsTable
import com.nintech.Database.Entities.TransactionEntity
import com.nintech.Database.Models.TransactionDto
import com.nintech.dbQuery
import java.util.UUID
import org.jetbrains.exposed.sql.transactions.transaction

class TransactionRepository {
    suspend fun findByUser(userId: String, limit: Int = 100, offset: Long = 0): List<TransactionDto> = dbQuery {
        TransactionEntity.find { TransactionsTable.userId eq userId }
            .limit(limit)
            .offset(offset.toInt().toLong())
            .map { it.toDto() }
    }

    suspend fun findById(id: String) = dbQuery {
        TransactionEntity.findById(id)?.toDto()
    }

    suspend fun create(userId: String, accountId: String, amount: Long, currency: String?, description: String?, date: java.time.LocalDate): TransactionDto = dbQuery {
        val id = UUID.randomUUID().toString()
        val ent = TransactionEntity.new(id) {
            this.userId = userId
            this.accountId = accountId
            this.amount = amount
            this.currency = currency
            this.description = description
            this.date = date
        }
        ent.toDto()
    }

    // create transfer: two transactions with same transferId
    suspend fun createTransfer(userId: String, fromAccountId: String, toAccountId: String, amount: Long, currency: String?, description: String?, date: java.time.LocalDate): List<TransactionDto> = dbQuery {
        val transferId = UUID.randomUUID().toString()
        transaction {
            val id1 = UUID.randomUUID().toString()
            val t1 = TransactionEntity.new(id1) {
                this.userId = userId
                this.accountId = fromAccountId
                this.amount = -amount // debit from source
                this.currency = currency
                this.description = description
                this.date = date
                this.transferId = transferId
            }

            val id2 = UUID.randomUUID().toString()
            val t2 = TransactionEntity.new(id2) {
                this.userId = userId
                this.accountId = toAccountId
                this.amount = amount // credit to destination
                this.currency = currency
                this.description = description
                this.date = date
                this.transferId = transferId
            }

            listOf(t1.toDto(), t2.toDto())
        }
    }

    suspend fun delete(id: String) = dbQuery {
        val ent = TransactionEntity.findById(id) ?: return@dbQuery false
        ent.delete()
        true
    }
}
