package com.tocaboca.tocacar.data

import android.app.Activity
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib

class ScoreCalculator {

    interface AppsFlyerConversionListenerWrapper : AppsFlyerConversionListener

    private val instance = AppsFlyerLib.getInstance()

    fun init(s: String, callback: AppsFlyerConversionListenerWrapper, activity: Activity) {
        instance.init(s, callback, activity)
    }

    fun start(activity: Activity) {
        instance.start(activity)
    }

    fun getAppsFlyerUID(activity: Activity) = AppsFlyerLib.getInstance().getAppsFlyerUID(activity)
}