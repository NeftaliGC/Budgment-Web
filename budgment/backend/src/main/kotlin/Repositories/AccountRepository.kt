package com.nintech.Repositories

import com.nintech.Database.Entities.AccountEntity
import com.nintech.Database.Models.AccountDto
import com.nintech.Database.Models.AccountWithBalance
import com.nintech.Database.AccountsTable
import com.nintech.dbQuery
import java.math.BigDecimal
import java.util.UUID
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class AccountRepository {
    suspend fun findByUser(userId: String): List<AccountDto> = dbQuery {
        AccountEntity.find { AccountsTable.userId eq userId }.map { it.toDto() }
    }

    suspend fun findById(id: String): AccountDto? = dbQuery {
        AccountEntity.findById(id)?.toDto()
    }

    suspend fun create(userId: String, name: String, currency: String): AccountDto = dbQuery {
        val id = UUID.randomUUID().toString()
        val ent = AccountEntity.new(id) {
            this.userId = userId
            this.name = name
            this.currency = currency
        }
        ent.toDto()
    }

    suspend fun existsForUser(accountId: String, userId: String): Boolean = dbQuery {
        val ent = AccountEntity.findById(accountId) ?: return@dbQuery false
        ent.userId == userId
    }

    // helper to compute balance by summing transactions.amount for the account
    suspend fun computeBalanceForAccount(accountId: String): AccountWithBalance = dbQuery {
        val acc = AccountEntity.findById(accountId) ?: throw IllegalArgumentException("Cuenta no encontrada")

        val sumLong = com.nintech.transactionsSumForAccount(accountId)

        val balanceDecimal = BigDecimal(sumLong).movePointLeft(2) // centavos -> unidades

        acc.toAccountWithBalanceDto(balanceDecimal)
    }

    // Nuevo: sumar todas las transacciones (amount) de todas las cuentas del usuario
    suspend fun computeTotalBalanceForUser(userId: String): Long = dbQuery {
        val tx = TransactionManager.current()
        val escaped = userId.replace("'", "''")
        val sql = "SELECT SUM(t.amount) AS s FROM transactions t JOIN accounts a ON t.account_id = a.id WHERE a.user_id = '$escaped'"
        tx.exec(sql) { rs ->
            if (rs.next()) {
                val v = rs.getLong(1)
                if (rs.wasNull()) 0L else v
            } else 0L
        } ?: 0L
    }

    // Nuevo: sumar ingresos (>0) del último mes para un usuario (retorna en centavos)
    suspend fun computeTotalIncomeLastMonthForUser(userId: String): Long = dbQuery {
        val tx = TransactionManager.current()
        val escaped = userId.replace("'", "''")
        val now = LocalDate.now()
        val start = now.minusMonths(1).withDayOfMonth(1)
        val end = start.with(TemporalAdjusters.lastDayOfMonth())
        val sql = "SELECT COALESCE(SUM(CASE WHEN t.amount > 0 THEN t.amount ELSE 0 END),0) AS s " +
                "FROM transactions t JOIN accounts a ON t.account_id = a.id " +
                "WHERE a.user_id = '$escaped' AND t.date >= '${start}' AND t.date <= '${end}'"
        tx.exec(sql) { rs ->
            if (rs.next()) {
                val v = rs.getLong(1)
                if (rs.wasNull()) 0L else v
            } else 0L
        } ?: 0L
    }

    // Nuevo: sumar gastos (<0) del último mes para un usuario (retorna positivo en centavos)
    suspend fun computeTotalExpenseLastMonthForUser(userId: String): Long = dbQuery {
        val tx = TransactionManager.current()
        val escaped = userId.replace("'", "''")
        val now = LocalDate.now()
        val start = now.minusMonths(1).withDayOfMonth(1)
        val end = start.with(TemporalAdjusters.lastDayOfMonth())
        val sql = "SELECT COALESCE(SUM(CASE WHEN t.amount < 0 THEN -t.amount ELSE 0 END),0) AS s " +
                "FROM transactions t JOIN accounts a ON t.account_id = a.id " +
                "WHERE a.user_id = '$escaped' AND t.date >= '${start}' AND t.date <= '${end}'"
        tx.exec(sql) { rs ->
            if (rs.next()) {
                val v = rs.getLong(1)
                if (rs.wasNull()) 0L else v
            } else 0L
        } ?: 0L
    }
}
