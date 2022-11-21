package com.dev.notificationservice

import android.app.Application
import android.os.Environment
import com.dev.notificationservice.di.AppComponent
import com.dev.notificationservice.di.DaggerAppComponent
import com.dev.notificationservice.di.modules.AppModule
import java.io.File

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        component.inject(this)

//        try {
//            val filename = "logcat_" + System.currentTimeMillis() + ".txt"
//            val outputfile = File(this.externalCacheDir, filename)
//            Runtime.getRuntime().exec("logcat -f"+outputfile.absoluteFile)
//        }
    }

    private val destinationFolder: File
        get() {
            val parent =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absoluteFile
            val destinationFolder = File(parent, "MyApp")
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs()
                destinationFolder.mkdir()
            }
            return destinationFolder
        }
}