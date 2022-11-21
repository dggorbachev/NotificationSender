package com.dev.notificationservice.di.modules

import android.app.Application
import com.dev.notificationservice.di.modules.data.DataModule
import com.dev.notificationservice.di.modules.network.ConnectionModule
import com.dev.notificationservice.di.modules.network.NetworkModule
import com.dev.notificationservice.di.modules.room.DataBaseModule
import dagger.Module
import dagger.Provides

@Module(includes = [DataModule::class, ConnectionModule::class, DataBaseModule::class, NetworkModule::class])
class AppModule(application: Application) {
    private val application: Application

    @Provides
    fun application(): Application {
        return application
    }

    init {
        this.application = application
    }
}