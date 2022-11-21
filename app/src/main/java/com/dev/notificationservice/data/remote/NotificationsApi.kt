package com.dev.notificationservice.data.remote

import com.dev.notificationservice.data.remote.model.NotificationRequestModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface NotificationsApi {
    @POST(".")
    suspend fun insert(
        @Body notification: NotificationRequestModel,
        @QueryMap options: Map<String, String>?
    )
}