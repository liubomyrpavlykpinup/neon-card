package com.tocaboca.tocacar.presentation.core

import android.app.Application
import com.onesignal.OneSignal

class GameEngineInitializer : EngineInitializer {
    override fun initialize(app: Application) {
        OneSignal.initWithContext(app)
        OneSignal.setAppId("a55eb853-728b-4f65-8fbe-d6a2e203b57a")
    }
}