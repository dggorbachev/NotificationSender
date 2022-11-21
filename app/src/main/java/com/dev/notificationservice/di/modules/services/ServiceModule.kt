package com.dev.notificationservice.di.modules.services

import com.dev.notificationservice.NotificationListener
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(mService: NotificationListener) {
    private val mService: NotificationListener

    init {
        this.mService = mService
    }

    @Provides
    fun provideMyService(): NotificationListener? {
        return mService
    }
}