package com.tocaboca.tocacar.presentation.launch

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.Settings

class LaunchService : Service() {

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): LaunchService = this@LaunchService
    }

    fun getAndroid(): String {
        return Settings.Global.getString(contentResolver, Settings.Global.ADB_ENABLED)
    }
}