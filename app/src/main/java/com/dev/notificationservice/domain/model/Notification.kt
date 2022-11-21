package com.dev.notificationservice.domain.model

import java.util.*

data class Notification(
    val id: UUID,
    val title: String?,
    val description: String?,
    val appName: String,
    val date: Long,
)