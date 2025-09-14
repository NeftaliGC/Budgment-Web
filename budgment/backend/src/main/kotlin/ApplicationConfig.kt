package com.nintech

import com.nintech.Repositories.UserRepository
import com.nintech.Services.UserService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationConfig = module {
    single { UserRepository() }
    single { UserService(get()) }

    single<String>(qualifier = named("secret")) {
        System.getenv("JWT_SECRET")
            ?: throw IllegalStateException("JWT_SECRET no configurado en el entorno")
    }
    single<String>(qualifier = named("issuer")) { "budgment-backend-api" }
    single<String>(qualifier = named("audience")) { "budgment-users" }

}
