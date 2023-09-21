package com.tocaboca.tocacar.presentation.launch

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tocaboca.tocacar.openMainActivity
import kotlinx.coroutines.launch
import kotlin.math.log

private const val TAG = "LaunchActivity"

@SuppressLint("CustomSplashScreen")
class LaunchActivity : AppCompatActivity() {

    private val viewModel by viewModels<LaunchViewModel>()

    private var launchService: LaunchService? = null
    private var bound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LaunchService.LocalBinder
            launchService = binder.getService()
            viewModel.setEvent(LaunchEvent.Launched(context = launchService))

            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            launchService = null
            bound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, LaunchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    when (it) {
                        LaunchState.Empty -> {}
                        is LaunchState.Configured -> {
                            openMainActivity(it.isAndroid)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}