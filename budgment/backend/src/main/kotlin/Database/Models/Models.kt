package com.nintech.Database.Models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val name: String?,
    val username: String?,
    val createdAt: String? = null,   // ISO-8601 strings recommended
    val updatedAt: String? = null
)

@Serializable
data class UserLogin(
    val username: String,
    val password: String
)

@Serializable
data class CreateUserRequest(
    val name: String,
    val username: String,
    val password: String
)

@Serializable
data class AccountDto(
    val id: String,
    val userId: String,
    val name: String,
    val currency: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)

@Serializable
data class AccountWithBalance(
    val id: String,
    val userId: String,
    val name: String,
    val currency: String,
    val balance: String,
    val balanceMinorUnits: Long? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)

@Serializable
data class CreateAccountRequest(
    val name: String,
    val currency: String,
    val initialBalance: String? = null
)

@Serializable
data class UpdateAccountRequest(
    val name: String? = null,
    val currency: String? = null
)

@Serializable
data class CategoryDto(
    val id: String,
    val userId: String,
    val name: String,
    val type: String,  // "Gasto" or "Ingreso"
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)

@Serializable
data class TransactionDto(
    val id: String,
    val userId: String,
    val accountId: String,
    val categoryId: String? = null,
    val amount: Long,                  // en centavos
    val currency: String? = null,
    val exchangeRateAtTransaction: Double? = null,
    val description: String? = null,
    val date: String,                  // YYYY-MM-DD
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val transferId: String? = null,
    val recurringId: String? = null
)

@Serializable
data class RecurringTransactionDto(
    val id: String,
    val userId: String,
    val accountId: String,
    val categoryId: String? = null,
    val baseAmount: Long,
    val currency: String? = null,
    val periodType: Int,
    val periodInterval: Int?,
    val periodUnit: String?,
    val anchorDate: String?,
    val nextOccurrence: String?,
    val endDate: String?,
    val active: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class BudgetDto(
    val id: String,
    val userId: String,
    val name: String,
    val amountLimit: Long,
    val scope: String,
    val periodType: Int,
    val periodInterval: Int?,
    val periodUnit: String?,
    val customRrule: String?,
    val startDate: String,
    val endDate: String,
    val autoCreateRecurring: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class BudgetCategoryDto(
    val id: String,
    val budgetId: String,
    val categoryId: String,
    val createdAt: String? = null
)

// --- Nuevos DTOs de request/updates para endpoints ---

@Serializable
data class CreateCategoryRequest(
    val name: String,
    val type: String
)

@Serializable
data class UpdateCategoryRequest(
    val name: String? = null,
    val type: String? = null
)

@Serializable
data class CreateTransactionRequest(
    val accountId: String,
    val amount: Long,
    val currency: String? = null,
    val categoryId: String? = null,
    val description: String? = null,
    val date: String,
    val transferToAccountId: String? = null
)

@Serializable
data class UpdateTransactionRequest(
    val accountId: String? = null,
    val amount: Long? = null,
    val currency: String? = null,
    val categoryId: String? = null,
    val description: String? = null,
    val date: String? = null
)

@Serializable
data class TotalBalanceDto(
    val userId: String,
    val total: String,           // decimal string
    val totalMinorUnits: Long    // centavos
)
