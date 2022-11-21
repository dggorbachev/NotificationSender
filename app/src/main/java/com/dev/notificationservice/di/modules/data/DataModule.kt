package com.dev.notificationservice.di.modules.data

import android.app.Application
import com.dev.notificationservice.preferences_manager.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LocalDataModule::class, RemoteDataModule::class])
object DataModule {
    @Provides
    @Singleton
    fun providePreferencesManager(
        application: Application,
    ): PreferencesManager =
        PreferencesManager(application)
}