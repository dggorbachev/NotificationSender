package com.dev.notificationservice

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.notificationservice.base.common.Constants.BASE_URL
import com.dev.notificationservice.data.local.LocalNotificationsRepo
import com.dev.notificationservice.databinding.ActivityMainBinding
import com.dev.notificationservice.preferences_manager.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var enableNotificationListenerAlertDialog: AlertDialog? = null

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var localNotificationsRepo: LocalNotificationsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).component
            .inject(this)

        bindLinkField()
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver,
            ENABLED_NOTIFICATION_LISTENERS)
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog? {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(R.string.yes
        ) { _, _ ->
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        alertDialogBuilder.setNegativeButton(R.string.no
        ) { _, _ ->
            // If you choose to not enable the notification listener
            // the app. will not work as expected
        }
        return alertDialogBuilder.create()
    }

    private fun bindLinkField() {
        with(binding) {

            CoroutineScope(Dispatchers.IO).launch {
                if (preferencesManager.systemPreferencesFlow.first().link != BASE_URL) {
                    withContext(Dispatchers.Main) {
                        tvStartLink.text =
                            getString(R.string.link_message,
                                preferencesManager.systemPreferencesFlow.first().link)
                    }
                }
            }

            bAccept.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {

                    var newLink = filledTextField.editText?.text.toString()
                    if (checkLink(newLink)) {
                        if (newLink[newLink.length - 1] != '/' && !newLink.contains("?")) {
                            newLink += '/'
                        }
                        preferencesManager.updateLink(newLink)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(application, "Link updated", Toast.LENGTH_SHORT)
                                .show()
                        }

                        withContext(Dispatchers.Main) {
                            if (getString(R.string.hint_first_link) == tvStartLink.text) {
                                if (!isNotificationServiceEnabled()) {
                                    enableNotificationListenerAlertDialog =
                                        buildNotificationServiceAlertDialog()
                                    enableNotificationListenerAlertDialog?.show()
                                }
                            }
                        }

                        withContext(Dispatchers.Main) {
                            tvStartLink.text =
                                getString(R.string.link_message,
                                    newLink)
                        }
                    }
                }
            }
        }
    }

    private suspend fun checkLink(newLink: String): Boolean {
        return if (!newLink.startsWith("https://") && !newLink.startsWith("http://")) {
            withContext(Dispatchers.Main) {
                Toast.makeText(application, "Incorrect link", Toast.LENGTH_SHORT)
                    .show()
            }
            false
        } else {
            try {
                withContext(Dispatchers.IO) {
                    val url = URL(newLink)
                    url.content
                }
                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(application, "Incorrect link", Toast.LENGTH_SHORT)
                        .show()
                }
                false
            }
        }
    }

    companion object {
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    }
}