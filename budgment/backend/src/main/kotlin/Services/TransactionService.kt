package com.nintech.Services

import com.nintech.Database.Models.CreateTransactionRequest
import com.nintech.Database.Models.TransactionDto
import com.nintech.Repositories.TransactionRepository
import com.nintech.Repositories.AccountRepository
import java.time.LocalDate
import java.time.format.DateTimeParseException

class TransactionService(private val repo: TransactionRepository, private val accountRepo: AccountRepository) {
    suspend fun listByUser(userId: String, limit: Int = 100, offset: Long = 0): List<TransactionDto> = repo.findByUser(userId, limit, offset)

    suspend fun getById(id: String): TransactionDto? = repo.findById(id)

    suspend fun create(userId: String, req: CreateTransactionRequest): TransactionDto {
        require(req.amount != 0L) { "El monto no puede ser cero" }
        val date = try {
            LocalDate.parse(req.date)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Fecha inválida, formato YYYY-MM-DD")
        }

        // validar que la cuenta pertenece al usuario
        val ok = accountRepo.existsForUser(req.accountId, userId)
        if (!ok) throw IllegalArgumentException("La cuenta no pertenece al usuario")

        return repo.create(userId, req.accountId, req.amount, req.currency, req.description, date)
    }

    suspend fun createTransfer(userId: String, req: CreateTransactionRequest): List<TransactionDto> {
        require(req.amount > 0L) { "El monto de transferencia debe ser mayor a cero" }
        val toAccount = req.transferToAccountId ?: throw IllegalArgumentException("transferToAccountId requerido para transferencias")
        val date = try {
            LocalDate.parse(req.date)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Fecha inválida, formato YYYY-MM-DD")
        }

        // validar ambas cuentas pertenecen al usuario
        val okFrom = accountRepo.existsForUser(req.accountId, userId)
        val okTo = accountRepo.existsForUser(toAccount, userId)
        if (!okFrom || !okTo) throw IllegalArgumentException("Ambas cuentas deben pertenecer al usuario")

        return repo.createTransfer(userId, req.accountId, toAccount, req.amount, req.currency, req.description, date)
    }

    suspend fun delete(id: String): Boolean = repo.delete(id)
}
