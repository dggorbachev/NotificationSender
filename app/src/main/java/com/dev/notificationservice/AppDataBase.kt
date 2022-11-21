package com.dev.notificationservice

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.notificationservice.data.local.NotificationsDAO
import com.dev.notificationservice.data.local.model.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun notificationsDAO(): NotificationsDAO
}