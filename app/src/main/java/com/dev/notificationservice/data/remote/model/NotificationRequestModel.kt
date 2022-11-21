package com.dev.notificationservice.data.remote.model

import com.google.gson.annotations.SerializedName

data class NotificationRequestModel(
    @SerializedName("notification")
    val notification: NotificationModel,
)