package com.dev.notificationservice.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.notificationservice.data.local.model.NotificationEntity

@Dao
interface NotificationsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM ${NotificationEntity.TABLE_NAME}")
    fun get(): List<NotificationEntity>

    @Query("DELETE FROM ${NotificationEntity.TABLE_NAME}")
    suspend fun deleteAll()
}