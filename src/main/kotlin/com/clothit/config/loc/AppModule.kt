package com.clothit.config.loc

import com.clothit.server.dao.ItemDao
import com.clothit.server.dao.impl.ItemDaoImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule {


    @Provides
    fun itemDao(): ItemDao {
        return ItemDaoImpl
    }
}