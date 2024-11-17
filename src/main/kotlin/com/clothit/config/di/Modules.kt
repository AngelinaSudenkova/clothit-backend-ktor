package com.clothit.config.di

import com.clothit.error.ErrorHandlingDelegate
import com.clothit.server.api.FileController
import com.clothit.server.api.ItemController
import com.clothit.server.api.OutfitController
import com.clothit.server.api.UserController
import com.clothit.server.dao.*
import com.clothit.server.dao.impl.*
import com.clothit.server.service.*
import com.clothit.server.service.impl.*
import org.koin.dsl.module

fun userModule() = module {
    single<UserDao> { UserDaoImpl }
    single<TokenDao> { TokenDaoImpl }
    single<UserService> { UserServiceImpl(get(), get()) }
    single<UserController> { UserController(get(), get()) }
}

fun outfitModule() = module {
    single<OutfitDao> { OutfitDaoImpl }
    single<ItemsToOutfitsDao> { ItemsToOutfitDaoImpl }
    single<OutfitService> { OutfitServiceImpl(get(), get(), get(), get()) }
    single<OutfitController> { OutfitController(get()) }
}

fun itemModule() = module {
    single<ItemDao> { ItemDaoImpl }
    single<ItemService> { ItemServiceImpl(get(), get()) }
    single<ItemController> { ItemController(get()) }
}

fun fileModule() = module {
    single<FileDao> { ErrorHandlingDelegate.create(FileDaoImpl) }

    single<FileService> { FileServiceImpl(get()) }
    single<FileController> { FileController(get()) }
}

fun authModule() = module {
    single<TokenDao> { ErrorHandlingDelegate.create(TokenDaoImpl) }
    single<TokenService> { TokenServiceImpl(get(), get()) }
}