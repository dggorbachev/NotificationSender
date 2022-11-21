package com.dev.notificationservice.preferences_manager

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dev.notificationservice.base.common.Constants.BASE_URL
import com.dev.notificationservice.base.dataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val context: Context) {

    val systemPreferencesFlow =
        context.dataStore.data.map { preferences ->
            val isFirstAppLaunch = preferences[PreferencesKeys.IS_FIRST_APP_LAUNCH] ?: true
            val link = preferences[PreferencesKeys.LINK] ?: BASE_URL

            SystemPreferences(isFirstAppLaunch, link)
        }

    suspend fun updateIsFirstAppLaunch(isFirstAppLaunch: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_APP_LAUNCH] = isFirstAppLaunch
        }
    }

    suspend fun updateLink(link: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LINK] = link
        }
    }

    private object PreferencesKeys {
        val IS_FIRST_APP_LAUNCH = booleanPreferencesKey("is_first_app_launch")
        val LINK = stringPreferencesKey("link")
    }
}