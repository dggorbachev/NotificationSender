package com.dev.notificationservice.data.remote

import com.dev.notificationservice.data.remote.model.NotificationModel
import com.dev.notificationservice.data.remote.model.NotificationRequestModel
import com.dev.notificationservice.domain.model.Notification

object RemoteMapper {

    fun Notification.toRequestModel() =
        NotificationRequestModel(
            notification = this.toRemoteModel()
        )

    private fun Notification.toRemoteModel() =
        NotificationModel(
            title = title,
            description = description,
            appName = appName,
            date = date
        )
}