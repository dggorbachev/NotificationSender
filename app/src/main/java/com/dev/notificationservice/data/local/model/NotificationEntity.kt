package com.dev.notificationservice.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.notificationservice.data.local.model.NotificationEntity.Companion.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
data class NotificationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "appName")
    val appName: String,
    @ColumnInfo(name = "date")
    val date: Long,
) {
    companion object {
        const val TABLE_NAME = "TASKS_TABLE"
    }
}