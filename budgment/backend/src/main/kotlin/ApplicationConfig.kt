package com.nintech

import com.nintech.Repositories.AccountRepository
import com.nintech.Repositories.CategoryRepository
import com.nintech.Repositories.TransactionRepository
import com.nintech.Repositories.UserRepository
import com.nintech.Services.AccountService
import com.nintech.Services.CategoryService
import com.nintech.Services.UserService
import com.nintech.Services.TransactionService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationConfig = module {
    single { UserRepository() }
    single { UserService(get()) }

    // nuevos repos y servicios
    single { AccountRepository() }
    single { TransactionRepository() }
    single { TransactionService(get(), get()) }
    single { AccountService(get(), get()) }

    single { CategoryRepository() }
    single { CategoryService(get()) }

    single<String>(qualifier = named("secret")) {
        System.getenv("JWT_SECRET")
            ?: throw IllegalStateException("JWT_SECRET no configurado en el entorno")
    }
    single<String>(qualifier = named("issuer")) { "budgment-backend-api" }
    single<String>(qualifier = named("audience")) { "budgment-users" }

}
