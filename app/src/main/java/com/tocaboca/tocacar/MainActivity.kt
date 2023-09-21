package com.tocaboca.tocacar

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.tocaboca.tocacar.presentation.load.LoadFragment
import com.tocaboca.tocacar.presentation.memory.MemoryFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            FragmentBuilder().build(this@MainActivity)
        }
    }

    inner class NeonCardView(context: Context) : WebView(context) {
        init {
            onBackPressedDispatcher.addCallback(this@MainActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (canGoBack()) {
                            goBack()
                        }
                    }
                })
        }
    }
}

class FragmentBuilder {

    fun build(activity: Activity) {
        val main = activity as? MainActivity ?: return
        val isAndroid = Navigation.getConfigurationValue(main)

        main.supportFragmentManager.commit {
            if (isAndroid.asBoolean()) {
                add(
                    R.id.fragment_container_view,
                    MemoryFragment.newInstance(param1 = "null", param2 = "1")
                )
            } else {
                add<LoadFragment>(R.id.fragment_container_view)
            }
            setReorderingAllowed(true)
        }
    }
}

fun String.asBoolean(): Boolean {
    return this == "1"
}