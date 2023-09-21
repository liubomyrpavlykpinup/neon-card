package com.tocaboca.tocacar.presentation.memory

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemoryViewModel : ViewModel() {

    private val _secondsRemaining = MutableStateFlow<Long>(1)
    val secondsRemaining = _secondsRemaining.asStateFlow()

    private val countDownTimer = object : CountDownTimer(60_000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished / 1000

            _secondsRemaining.value = secondsRemaining
        }

        override fun onFinish() {
            _secondsRemaining.value = 1
        }
    }

    fun startTimer() {
        countDownTimer.start()
    }
}