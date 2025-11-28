package com.nintech.Services

import com.nintech.Database.Models.AccountDto
import com.nintech.Database.Models.CreateAccountRequest
import com.nintech.Database.Models.AccountWithBalance
import com.nintech.Database.Models.TotalBalanceDto
import com.nintech.Database.Models.CreateTransactionRequest
import com.nintech.Repositories.AccountRepository
import java.math.BigDecimal
import java.time.LocalDate

class AccountService(private val repo: AccountRepository, private val txService: TransactionService) {
    suspend fun listByUser(userId: String): List<AccountDto> = repo.findByUser(userId)

    suspend fun getById(id: String): AccountDto? = repo.findById(id)

    suspend fun create(userId: String, req: CreateAccountRequest): AccountDto {
        require(req.name.isNotBlank()) { "Nombre de cuenta vacío" }
        require(req.currency.length == 3) { "Currency inválido" }
        val created = repo.create(userId, req.name, req.currency)

        // Si el request incluye initialBalance, crear una transacción inicial para reflejarlo
        val init = req.initialBalance?.trim()?.takeIf { it.isNotEmpty() }
        if (init != null) {
            try {
                val bd = BigDecimal(init)
                val cents = bd.movePointRight(2).toLong()
                if (cents != 0L) {
                    val txReq = CreateTransactionRequest(
                        accountId = created.id,
                        amount = cents,
                        currency = req.currency,
                        categoryId = null,
                        description = "Initial balance",
                        date = LocalDate.now().toString(),
                        transferToAccountId = null
                    )
                    // crear transacción inicial
                    txService.create(userId, txReq)
                }
            } catch (e: Exception) {
                // No fallamos la creación de la cuenta si el initialBalance no es válido,
                // pero logueamos la situación (el servidor lo registrará en logs).
                println("Warning: initialBalance invalido: ${e.message}")
            }
        }

        return created
    }

    suspend fun getWithBalance(accountId: String): AccountWithBalance = repo.computeBalanceForAccount(accountId)

    // Nuevo: obtener el total del balance de todas las cuentas del usuario
    suspend fun totalBalanceForUser(userId: String): TotalBalanceDto {
        val sumLong = repo.computeTotalBalanceForUser(userId)
        val totalDecimal = BigDecimal(sumLong).movePointLeft(2)
        return TotalBalanceDto(userId = userId, total = totalDecimal.toPlainString(), totalMinorUnits = sumLong)
    }

    // Nuevo: ingresos del último mes
    suspend fun incomeLastMonthForUser(userId: String): TotalBalanceDto {
        val sumLong = repo.computeTotalIncomeLastMonthForUser(userId)
        val totalDecimal = BigDecimal(sumLong).movePointLeft(2)
        return TotalBalanceDto(userId = userId, total = totalDecimal.toPlainString(), totalMinorUnits = sumLong)
    }

    // Nuevo: gastos del último mes
    suspend fun expenseLastMonthForUser(userId: String): TotalBalanceDto {
        val sumLong = repo.computeTotalExpenseLastMonthForUser(userId)
        val totalDecimal = BigDecimal(sumLong).movePointLeft(2)
        return TotalBalanceDto(userId = userId, total = totalDecimal.toPlainString(), totalMinorUnits = sumLong)
    }
}
