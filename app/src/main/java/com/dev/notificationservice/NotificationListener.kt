package com.dev.notificationservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.dev.notificationservice.base.connection.ConnectionLiveData
import com.dev.notificationservice.base.connection.ConnectionRepo
import com.dev.notificationservice.data.local.LocalNotificationsRepo
import com.dev.notificationservice.data.remote.RemoteNotificationsRepo
import com.dev.notificationservice.domain.model.Notification
import com.dev.notificationservice.preferences_manager.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Retrofit
import java.lang.reflect.Field
import java.util.*
import javax.inject.Inject


class NotificationListener @Inject constructor() : NotificationListenerService() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData

    @Inject
    lateinit var localNotificationsRepo: LocalNotificationsRepo

    @Inject
    lateinit var remoteNotificationsRepo: RemoteNotificationsRepo

    @Inject
    lateinit var connectionRepo: ConnectionRepo

    @Inject
    lateinit var retrofitInstance: Retrofit

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {

        super.onCreate()

        (application as App).component.inject(this)

        checkConnection()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.equals(Intent(Intent.ACTION_PACKAGE_CHANGED)) == true) {
            val pm: PackageManager = packageManager
            pm.setComponentEnabledSetting(ComponentName(this,
                NotificationListener::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
            pm.setComponentEnabledSetting(ComponentName(this,
                NotificationListener::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {

        return super.onBind(intent)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        scope.launch {
            if (activeNotifications.isNotEmpty())

                if (preferencesManager.systemPreferencesFlow.first().isFirstAppLaunch) {
                    preferencesManager.updateIsFirstAppLaunch(false)

                    if (!connectionRepo.hasConnection()) {
                        this@NotificationListener.activeNotifications.forEach {
                            try {
//                                Log.d("callFromHere5message",
//                                    it.notification.tickerText?.toString() ?: "")
                                saveLocal(getModel(it))
                            } catch (e: Exception) {
                                // random exception catch
                            }
                        }
                    } else {

                        val link = preferencesManager.systemPreferencesFlow.first().link

                        if (link != "") {
                            this@NotificationListener.activeNotifications.forEach {
                                try {
//                                    Log.d("callFromHere4", "")
                                    saveRemote(getModel(it))
                                } catch (e: Exception) {
                                    // random exception catch
                                }
                            }
                        }
                    }
                }

        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        scope.launch {
            if (!connectionRepo.hasConnection()) {
                try {
                    checkConnection()
//                    Log.d("callFromHere3", "")
//                    Log.d("callFromHere3message", sbn.notification.tickerText?.toString() ?: "")
                    saveLocal(getModel(sbn))
                } catch (e: Exception) {
                    // random exception catch
                }
            } else {
                try {
                    sbn.notification.extras.get(android.app.Notification.EXTRA_BIG_TEXT)

                    saveRemote(getModel(sbn))
                } catch (e: Exception) {
//                    random exception catch
                }
            }
        }
    }

    private suspend fun saveLocal(notification: Notification) {
        if (notification.appName != "com.google.android.setupwizard" && notification.appName != "org.telegram.messenger") {
            if (notification.title != null || notification.description != null)
                localNotificationsRepo.insert(notification)
        }
    }


    private suspend fun saveRemote(notification: Notification) {
        (application as App).component.inject(this)

        if (notification.appName != "com.google.android.setupwizard" && notification.appName != "org.telegram.messenger") {
            if (notification.title != null || notification.description != null) {

                val link = preferencesManager.systemPreferencesFlow.first().link

                // hack retrofit
                val field: Field = Retrofit::class.java.getDeclaredField("baseUrl")
                field.isAccessible = true
                val newHttpUrl = link.toHttpUrlOrNull()
                field.set(retrofitInstance, newHttpUrl)

                remoteNotificationsRepo.insert(notification)
            }
        }
    }

    private fun getModel(sbn: StatusBarNotification): Notification {
        val title = sbn.notification.tickerText?.toString()
        val description: String? =
            sbn.notification.extras.get(android.app.Notification.EXTRA_BIG_TEXT)?.toString()

        val appName = sbn.packageName
        val date = sbn.notification.`when`
        return Notification(UUID.randomUUID(), title, description, appName, date)
    }

    private fun checkConnection() {
        connectionLiveData.setData(false)
        connectionLiveData.observeForever { isNetworkAvailable ->
            scope.launch {
                if (isNetworkAvailable) {
                    connectionRepo.setConnection(true)

                    try {
                        localNotificationsRepo.get().forEach {
                            saveRemote(it)
                        }

                        localNotificationsRepo.deleteAll()
                    } catch (e: Exception) {
                        Log.d("exception", e.localizedMessage ?: "")
                        // random exception catch
                    }
                }
                if (!isNetworkAvailable) {
                    connectionRepo.setConnection(false)
                }
            }
        }
    }

    private fun checkInternet(): Boolean {

        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}