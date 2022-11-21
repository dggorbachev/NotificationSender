package com.dev.notificationservice.di.modules.network

import android.app.Application
import android.net.ConnectivityManager
import com.dev.notificationservice.base.connection.ConnectionLiveData
import com.dev.notificationservice.base.connection.ConnectionRepo
import com.dev.notificationservice.base.connection.ConnectionRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ConnectionModule {

    @Provides
    @Singleton
    fun provideConnectionLiveData(
        application: Application,
    ): ConnectionLiveData =
        ConnectionLiveData(application, ConnectivityManager.NetworkCallback())

    @Provides
    @Singleton
    fun provideConnectionRepo(
    ): ConnectionRepo =
        ConnectionRepoImpl()
}