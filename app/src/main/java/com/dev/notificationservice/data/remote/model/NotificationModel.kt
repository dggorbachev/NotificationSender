package com.dev.notificationservice.data.remote.model

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("appName")
    val appName: String,
    @SerializedName("date")
    val date: Long,
)