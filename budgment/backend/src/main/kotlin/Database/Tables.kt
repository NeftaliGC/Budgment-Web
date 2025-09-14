package com.nintech.Database

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDateTime

object UsersTable : IdTable<String>("users") {
    override val id = varchar("id", 36).entityId()
    val name = text("name").nullable()
    val username = varchar("username", 100).uniqueIndex().nullable()
    val passwordHash = text("password_hash").nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    override val primaryKey = PrimaryKey(id)
}

object AccountsTable : IdTable<String>("accounts") {
    override val id = varchar("id", 36).entityId()
    val userId = varchar("user_id", 36).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 200)
    val currency = varchar("currency", 3)
    // En SQLite el blob se guarda en BLOB; Exposed -> binary
    // Ajusta length según tu cifrado (ej. 4096)
    val balanceCacheEncrypted = binary("balance_cache_encrypted", 4096).nullable()
    val balanceIv = binary("balance_iv", 64).nullable()
    val balanceEncVersion = integer("balance_enc_version").default(1)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    val deletedAt = datetime("deleted_at").nullable()
    override val primaryKey = PrimaryKey(id)
    // index por usuario
    init {
        index(true, userId)
    }
}

object CategoriesTable : Table("categories") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 36).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 200)
    val type = varchar("type", 20) // 'Gasto'|'Ingreso' — valida en app
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    val deletedAt = datetime("deleted_at").nullable()
    override val primaryKey = PrimaryKey(id)
    init {
        index(true, userId)
    }
}

object RecurringTransactionsTable : Table("recurring_transactions") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 36).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val accountId = varchar("account_id", 36).references(AccountsTable.id, onDelete = ReferenceOption.RESTRICT)
    val categoryId = varchar("category_id", 36).references(CategoriesTable.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val baseAmount = long("base_amount")
    val currency = varchar("currency", 3).nullable()
    val periodType = integer("period_type")
    val periodInterval = integer("period_interval").default(1)
    val periodUnit = varchar("period_unit", 10).nullable()
    val anchorDate = date("anchor_date").nullable()
    val nextOccurrence = date("next_occurrence").nullable()
    val endDate = date("end_date").nullable()
    val active = integer("active").default(1) // 0/1
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    override val primaryKey = PrimaryKey(id)
    init {
        index(false, nextOccurrence)
        index(false, userId)
    }
}

object TransactionsTable : Table("transactions") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 36).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val accountId = varchar("account_id", 36).references(AccountsTable.id, onDelete = ReferenceOption.RESTRICT)
    val categoryId = varchar("category_id", 36).references(CategoriesTable.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val amount = long("amount") // centavos
    val currency = varchar("currency", 3).nullable()
    val exchangeRateAtTransaction = double("exchange_rate_at_transaction").nullable()
    val description = text("description").nullable()
    val date = date("date")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    val transferId = varchar("transfer_id", 36).nullable()
    val recurringId = varchar("recurring_id", 36).references(RecurringTransactionsTable.id, onDelete = ReferenceOption.SET_NULL).nullable()
    override val primaryKey = PrimaryKey(id)
    init {
        index(false, userId, date)
        index(false, accountId, date)
        index(false, categoryId, date)
        index(false, transferId)
    }
}

object BudgetsTable : Table("budgets") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 36).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 200)
    val amountLimit = long("amount_limit")
    val scope = varchar("scope", 20) // 'Global'|'Category'
    val periodType = integer("period_type")
    val periodInterval = integer("period_interval").default(1)
    val periodUnit = varchar("period_unit", 10).nullable()
    val customRrule = text("custom_rrule").nullable()
    val startDate = date("start_date")
    val endDate = date("end_date")
    val autoCreateRecurring = integer("auto_create_recurring").default(0)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
    override val primaryKey = PrimaryKey(id)
    init {
        index(false, userId, startDate, endDate, createdAt)
    }
}

object BudgetCategoriesTable : Table("budget_categories") {
    val id = varchar("id", 36)
    val budgetId = varchar("budget_id", 36).references(BudgetsTable.id, onDelete = ReferenceOption.CASCADE)
    val categoryId = varchar("category_id", 36).references(CategoriesTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    override val primaryKey = PrimaryKey(id)
    init {
        uniqueIndex(budgetId, categoryId)
    }
}
