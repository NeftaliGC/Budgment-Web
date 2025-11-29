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

    install(Sessions) {
        cookie<UserSession>("SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.secure = true                     // OBLIGATORIO EN HTTPS
            cookie.maxAgeInSeconds = 15 * 60         // Igual al access token
            cookie.sameSite = SameSite.None          // OBLIGATORIO ENTRE DOMINIOS
            cookie.domain = "tudominio.com"          // IMPORTANTE
        }

        cookie<RefreshSession>("REFRESH") {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.secure = true
            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60
            cookie.sameSite = SameSite.None
            cookie.domain = "tudominio.com"
        }
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
