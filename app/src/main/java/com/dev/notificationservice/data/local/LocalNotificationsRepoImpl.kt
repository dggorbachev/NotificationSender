package com.dev.notificationservice.data.local

import com.dev.notificationservice.data.local.LocalMapper.toDomainModel
import com.dev.notificationservice.data.local.LocalMapper.toEntityModel
import com.dev.notificationservice.domain.model.Notification
import javax.inject.Inject

class LocalNotificationsRepoImpl @Inject constructor(private val notificationsDAO: NotificationsDAO) :
    LocalNotificationsRepo {

    override suspend fun insert(notification: Notification) =
        notificationsDAO.insert(notification.toEntityModel())

    override suspend fun get(): List<Notification> =
        notificationsDAO.get().map { it.toDomainModel() }

    override suspend fun deleteAll() = notificationsDAO.deleteAll()
}