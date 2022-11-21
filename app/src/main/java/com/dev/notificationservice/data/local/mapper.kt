package com.dev.notificationservice.data.local

import com.dev.notificationservice.data.local.model.NotificationEntity
import com.dev.notificationservice.domain.model.Notification

object LocalMapper {

    fun NotificationEntity.toDomainModel() =
        Notification(
            id = id,
            title = title,
            description = description,
            appName = appName,
            date = date
        )

    fun Notification.toEntityModel() =
        NotificationEntity(
            id = id,
            title = title,
            description = description,
            appName = appName,
            date = date
        )
}