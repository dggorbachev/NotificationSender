package com.dev.notificationservice.di

import android.app.Application
import com.dev.notificationservice.App
import com.dev.notificationservice.MainActivity
import com.dev.notificationservice.NotificationListener
import com.dev.notificationservice.di.modules.AppModule
import com.dev.notificationservice.di.modules.services.ServiceModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ServiceModule::class])
@Singleton
interface AppComponent {
    fun application(): Application
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
    fun inject(notificationListener: NotificationListener)
}