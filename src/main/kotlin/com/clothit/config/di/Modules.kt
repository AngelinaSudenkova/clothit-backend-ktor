package com.clothit.config.di

import com.clothit.server.api.FriendController
import com.clothit.server.api.FileController
import com.clothit.server.api.ItemController
import com.clothit.server.api.OutfitController
import com.clothit.server.api.UserController
import com.clothit.server.dao.*
import com.clothit.server.dao.impl.*
import com.clothit.server.service.*
import com.clothit.server.service.impl.*
import com.example.server.dao.FriendDao
import org.koin.dsl.module

fun appModule() = module {
    single<UserDao> { UserDaoImpl }
    single<TokenDao> { TokenDaoImpl }
    single<UserService> { UserServiceImpl(get(), get()) }
    single<UserController> { UserController(get(), get()) }

    single<TokenDao> { TokenDaoImpl }
    single<JwtService> { JwtServiceImpl(get(), get()) }
    single<JwtServiceImpl> { JwtServiceImpl(get(), get()) }

    single<FileDao> { FileDaoImpl }
    single<FileService> { FileServiceImpl(get()) }
    single<FileController> { FileController(get()) }

    single<ItemDao> { ItemDaoImpl }
    single<ItemService> { ItemServiceImpl(get(), get()) }
    single<ItemController> { ItemController(get()) }

    single<OutfitDao> { OutfitDaoImpl }
    single<ItemsToOutfitsDao> { ItemsToOutfitDaoImpl }
    single<OutfitService> { OutfitServiceImpl(get(), get(), get(), get()) }
    single<OutfitController>{OutfitController(get())}

    single<FriendDao> { FriendDaoImpl }
    single<FriendService> { FriendServiceImpl(get(), get()) }
    single<FriendController> { FriendController(get()) }
}