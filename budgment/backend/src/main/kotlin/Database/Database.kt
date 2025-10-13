package com.nintech

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

object DatabaseFactory {
    private var dataSource: HikariDataSource? = null

    fun connect(url: String = "jdbc:sqlite:./data/budgment.sqlite") {
        val dbFile = File(".data/budgment.sqlite")
        val isNewDatabase = !dbFile.exists()

        val config = HikariConfig().apply {
            jdbcUrl = url
            driverClassName = "org.sqlite.JDBC"
            maximumPoolSize = 8
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
        }
        dataSource = HikariDataSource(config)
        Database.connect(dataSource!!)

        if(isNewDatabase){
            println("Nueva base de datos detectada, inicializando desde seed.sql...")
            initializeFromSeed(url)
        } else if (!hasTables(url)) {
            println("Base de datos existente pero sin tablas, ejecutando seed.sql...")
            initializeFromSeed(url)
        }
    }

    fun close() {
        dataSource?.close()
    }

    private fun hasTables(url: String): Boolean {
        DriverManager.getConnection(url).use { conn ->
            conn.createStatement().use { stmt ->
                val rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';")
                return rs.next()
            }
        }
    }

    private fun initializeFromSeed(url: String) {
        val seedSql = this::class.java.getResource("/seed.sql")?.readText()
            ?: error("Seed no encontrada")

        DriverManager.getConnection(url).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate("PRAGMA foreign_keys = OFF;")
                stmt.executeUpdate("BEGIN TRANSACTION;")
                stmt.executeUpdate(seedSql)
                stmt.executeUpdate("COMMIT;")
                stmt.executeUpdate("PRAGMA foreign_keys = ON;")
            }
        }
    }

}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
