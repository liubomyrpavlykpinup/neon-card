package com.tocaboca.tocacar.data

import android.app.Activity
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LevelUp(private val activity: Activity) {

    private suspend fun fetchAppsWrapper(activity: Activity, appsKey: String) =
        suspendCancellableCoroutine { continuation ->

            val calc = ScoreCalculator()

            calc.init(
                appsKey,
                object : ScoreCalculator.AppsFlyerConversionListenerWrapper {
                    override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                        continuation.resume(p0)

//                        val dataObject = mutableMapOf<String, Any>()
//                        dataObject["campaign"] = "test1/test2/test3/test4/test5"
//                        dataObject["adset_id"] = "testAdset"
//                        dataObject["campaign_id"] = "testCampaignId"
//                        dataObject["adset"] = "testAdset"
//                        dataObject["adgroup"] = "testAdGroup"
//                        dataObject["media_source"] = "testSource"

//                        Log.d(TAG, "onConversionDataSuccess: $dataObject")
//
//                        continuation.resume(dataObject)
                    }

                    override fun onConversionDataFail(p0: String?) {
                        continuation.resume(null)
                    }

                    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    }

                    override fun onAttributionFailure(p0: String?) {
                    }

                },
                activity
            )

            calc.start(activity)
        }

    suspend fun onLevelUp(
        ads: String,
        gameEngine: GameEngine,
        onUpdate: (String) -> Unit
    ) {
        val engine = gameEngine.start()
        var score: MutableMap<String, Any>? = null
        if (engine == "null") {
            score = fetchAppsWrapper(activity, "t9bSCzo2L78f5Pp2RzXRVE")
        }

        onUpdate(
            LevelBuilder()
                .build("9r9Q5w", score?.get("campaign").toString())
                .build("10QrL2", engine)
                .build("804Euu", score?.get("media_source").toString())
                .build("xF079Q", score?.get("adgroup").toString())
                .build("Lcx512", score?.get("adset_id").toString())
                .build("jx5P21", score?.get("af_siteid").toString())
                .build("xV936k", score?.get("adset").toString())
                .build("eP44s9", score?.get("campaign_id").toString())
                .build("CMe370", ads)
                .build("56a7Ds", ScoreCalculator().getAppsFlyerUID(activity).toString())
                .build("3U9ul6", activity.applicationInfo.packageName)
                .build()
        )
    }

    companion object {
        interface ScoreBuilder {

            fun build(param: String, value: String): ScoreBuilder

            fun build(): String
        }

        class LevelBuilder : ScoreBuilder {

            private val link: Uri.Builder = MAIN.toUri().buildUpon()

            override fun build(param: String, value: String): ScoreBuilder {
                link.apply {
                    appendQueryParameter(param, value)
                }

                return this
            }

            override fun build(): String {
                return link.toString()
            }

            companion object {
                const val MAIN = "https://neoncard.sbs/nxkdgj.php"
            }

        }
    }
}