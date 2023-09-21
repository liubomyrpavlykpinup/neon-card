package com.tocaboca.tocacar.presentation.launch

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchService : Service() {

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): LaunchService = this@LaunchService
    }

    suspend fun getAndroid(): String = withContext(Dispatchers.IO) {
        return@withContext Settings.Global.getString(contentResolver, Settings.Global.ADB_ENABLED)
    }
}