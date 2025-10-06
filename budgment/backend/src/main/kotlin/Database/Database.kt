package com.nintech

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    private var dataSource: HikariDataSource? = null

    fun connect(url: String = "jdbc:sqlite:./data/budgment.sqlite") {
        val config = HikariConfig().apply {
            jdbcUrl = url
            driverClassName = "org.sqlite.JDBC"
            maximumPoolSize = 8
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
        }
        dataSource = HikariDataSource(config)
        Database.connect(dataSource!!)
    }

    fun close() {
        dataSource?.close()
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
