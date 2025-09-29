package com.nintech

import com.nintech.Auth.authConfig
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(applicationConfig)
    }

    authConfig()
    configureMonitoring()
    configureHTTP()
    configureRouting()
    DatabaseFactory.connect()

    monitor.subscribe(ApplicationStopping) {
        println("Stoping server")
        DatabaseFactory.close()
    }
}
