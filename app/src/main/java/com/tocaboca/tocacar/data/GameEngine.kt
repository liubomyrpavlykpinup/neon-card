package com.tocaboca.tocacar.data

import android.app.Activity
import com.facebook.applinks.AppLinkData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GameEngine(private val activity: Activity) {

    suspend fun start() =
        suspendCancellableCoroutine { continuation ->
            AppLinkData.fetchDeferredAppLinkData(activity) {
                continuation.resume(it?.targetUri.toString())
//                continuation.resume("myapp://test1/test2/test3/test4/test5")
            }
        }
}

