package com.tocaboca.tocacar.presentation.launch

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LaunchEvent {
    class Launched(private val context: Context?) : LaunchEvent {

        fun getConfigurationService(): LaunchService? {
            return context as? LaunchService
        }
    }

}

sealed interface LaunchState {
    object Empty : LaunchState
    data class Configured(val isAndroid: String) : LaunchState
}

class LaunchViewModel : ViewModel() {

    private val _state = MutableStateFlow<LaunchState>(LaunchState.Empty)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<LaunchEvent>()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            _events.collect {
                if (it is LaunchEvent.Launched) {
                    val service = it.getConfigurationService()

                    _state.emit(LaunchState.Configured(isAndroid = service?.getAndroid() ?: "1"))
                }
            }
        }
    }

    fun setEvent(event: LaunchEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}