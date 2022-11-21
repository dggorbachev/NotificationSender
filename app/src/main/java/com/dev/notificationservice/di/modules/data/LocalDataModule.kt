package com.dev.notificationservice.di.modules.data

import com.dev.notificationservice.data.local.LocalNotificationsRepo
import com.dev.notificationservice.data.local.LocalNotificationsRepoImpl
import com.dev.notificationservice.data.local.NotificationsDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalDataModule {

    @Provides
    @Singleton
    fun provideLocalTasksRepo(notificationsDAO: NotificationsDAO): LocalNotificationsRepo =
        LocalNotificationsRepoImpl(notificationsDAO = notificationsDAO)
}