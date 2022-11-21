package com.dev.notificationservice.data.remote

import com.dev.notificationservice.domain.model.Notification

interface RemoteNotificationsRepo {
    suspend fun insert(notification: Notification)
}