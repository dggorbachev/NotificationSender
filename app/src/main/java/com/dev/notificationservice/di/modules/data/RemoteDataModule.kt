package com.dev.notificationservice.di.modules.data

import com.dev.notificationservice.data.remote.NotificationsApi
import com.dev.notificationservice.data.remote.NotificationsRemoteSource
import com.dev.notificationservice.data.remote.RemoteNotificationsRepo
import com.dev.notificationservice.data.remote.RemoteNotificationsRepoImpl
import com.dev.notificationservice.preferences_manager.PreferencesManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object RemoteDataModule {

    @Provides
    @Singleton
    fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi =
        retrofit.create(NotificationsApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationsRemoteSource(
        notificationsApi: NotificationsApi,
        preferencesManager: PreferencesManager,
    ): NotificationsRemoteSource =
        NotificationsRemoteSource(notificationsApi = notificationsApi, preferencesManager)

    @Provides
    @Singleton
    fun provideRemoteNotificationsRepo(
        source: NotificationsRemoteSource,
    ): RemoteNotificationsRepo =
        RemoteNotificationsRepoImpl(source)
}