package com.dev.notificationservice.data.remote

import android.util.Log
import com.dev.notificationservice.data.remote.model.NotificationRequestModel
import com.dev.notificationservice.preferences_manager.PreferencesManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationsRemoteSource @Inject constructor(
    private val notificationsApi: NotificationsApi,
    private val preferencesManager: PreferencesManager,
) {


    suspend fun insert(notificationRequestModel: NotificationRequestModel) {
        var link = preferencesManager.systemPreferencesFlow.first().link
        val data: MutableMap<String, String> = HashMap()


        if (link.contains("?")) {
            link = link.substring(link.indexOf("?") + 1)

            while (link != "") {
                val firstEquals = link.indexOfFirst { it == '=' }
                val firstAnd =
                    if (link.indexOfFirst { it == '&' } == -1) link.length else link.indexOfFirst { it == '&' }

                val param = link.substring(0, firstEquals)
                val mean =
                    link.substring(firstEquals + 1,
                        firstAnd)
                data[param] = mean
                link = link.substring(if (firstAnd == link.length) firstAnd else firstAnd + 1)
            }
        }

        notificationsApi.insert(notification = notificationRequestModel,
                data)
    }
}