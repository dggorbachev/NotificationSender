package com.dev.notificationservice.di.modules.room

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.dev.notificationservice.AppDataBase
import com.dev.notificationservice.base.common.Constants.DATA_BASE
import com.dev.notificationservice.data.local.NotificationsDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@NonNull application: Application): AppDataBase {
        return Room
            .databaseBuilder(application, AppDataBase::class.java, DATA_BASE)
            .build()
    }

    @Provides
    @Singleton
    fun provideChannelDao(appDatabase: AppDataBase): NotificationsDAO {
        return appDatabase.notificationsDAO()
    }
}