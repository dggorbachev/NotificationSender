package com.dev.notificationservice.base.connection

class ConnectionRepoImpl : ConnectionRepo {

    private var connection: Connection = Connection.CONNECTED
    private var previousConnection: Connection = connection

    override fun setConnection(isConnected: Boolean) {
        previousConnection = connection
        connection = when (isConnected) {
            true -> Connection.CONNECTED
            false -> Connection.DISCONNECTED
        }
    }

    override fun hasConnection(): Boolean {
        return when (connection) {
            Connection.CONNECTED -> true
            Connection.DISCONNECTED -> false
        }
    }

    override fun previousStateHasConnection(): Boolean {
        return when (previousConnection) {
            Connection.CONNECTED -> true
            Connection.DISCONNECTED -> false
        }
    }
}