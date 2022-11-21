package com.dev.notificationservice.base.connection

interface ConnectionRepo {
    fun setConnection(isConnected: Boolean)

    fun hasConnection(): Boolean

    fun previousStateHasConnection(): Boolean
}