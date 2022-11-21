package com.dev.notificationservice.data.local

import com.dev.notificationservice.domain.model.Notification

interface LocalNotificationsRepo {
    suspend fun insert(notification: Notification)
    suspend fun get(): List<Notification>
    suspend fun deleteAll()
}