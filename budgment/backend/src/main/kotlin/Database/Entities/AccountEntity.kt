package com.nintech.Database.Entities

import com.nintech.Database.AccountsTable
import com.nintech.Database.Models.AccountDto
import com.nintech.Database.Models.AccountWithBalance
import com.nintech.Database.Models.UserDto
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.math.BigDecimal

class AccountEntity(id: EntityID<String>): Entity<String>(id) {
    companion object: EntityClass<String, AccountEntity>(AccountsTable)

    var userId by AccountsTable.userId
    var name by AccountsTable.name
    var currency by AccountsTable.currency
    var balanceCacheEncrypted by AccountsTable.balanceCacheEncrypted
    val balanceIV by AccountsTable.balanceIv
    val balanceEncVersion by AccountsTable.balanceEncVersion
    val createdAt by AccountsTable.createdAt
    val updatedAt by AccountsTable.updatedAt
    val deletedAt by AccountsTable.deletedAt

    fun toDto() = AccountDto(
        id = id.value,
        userId = userId,
        name = name,
        currency = currency,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString(),
        deletedAt = deletedAt?.toString()
    )

    fun toAccountWithBalanceDto(balanceDecimal: BigDecimal) = AccountWithBalance(
        id = id.value,
        userId = userId,
        name = name,
        currency = currency,
        balance = balanceDecimal.toPlainString(),
        balanceMinorUnits = (balanceDecimal.movePointRight(2).toLong()),
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString(),
        deletedAt = deletedAt?.toString()
    )
}