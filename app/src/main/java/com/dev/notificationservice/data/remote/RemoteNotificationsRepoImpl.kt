package com.dev.notificationservice.data.remote

import android.util.Log
import com.dev.notificationservice.data.remote.RemoteMapper.toRequestModel
import com.dev.notificationservice.domain.model.Notification
import javax.inject.Inject

class RemoteNotificationsRepoImpl @Inject constructor(
    private val source: NotificationsRemoteSource,
) :
    RemoteNotificationsRepo {


    override suspend fun insert(notification: Notification) {
        source.insert(notification.toRequestModel())
    }
}