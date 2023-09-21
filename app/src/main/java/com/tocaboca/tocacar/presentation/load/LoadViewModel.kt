package com.tocaboca.tocacar.presentation.load

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tocaboca.tocacar.data.GameEngine
import com.tocaboca.tocacar.data.GoogleIdentifierReceiver
import com.tocaboca.tocacar.data.LevelUp
import com.tocaboca.tocacar.data.NeonCardRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoadState {
    object Loading : LoadState
    data class Loaded(val name: String) : LoadState
}

sealed interface LoadEvent {
    data class Load(val activity: Activity) : LoadEvent
}

class LoadViewModel(
    private val neonCardRepository: NeonCardRepository
) : ViewModel() {

    private val _loadState =
        MutableStateFlow<LoadState>(LoadState.Loading)
    val configurationState = _loadState.asStateFlow()

    private val events = MutableSharedFlow<LoadEvent>()

    init {
        viewModelScope.launch {
            events.collect {
                if (it is LoadEvent.Load) {
                    val name = neonCardRepository.neonCard()?.name
                    if (name.isNullOrBlank()) {
                        initialLaunch(it.activity)
                    } else {
                        relaunch(name = name)
                    }
                }
            }
        }
    }

    private fun initialLaunch(activity: Activity) {
        viewModelScope.launch {
            val ads = GoogleIdentifierReceiver.get(context = activity)

            LevelUp(activity)
                .onLevelUp(
                    ads = ads,
                    gameEngine = GameEngine(activity),
                    onUpdate = {
                        _loadState.value = LoadState.Loaded(name = it)
                    })
        }
    }

    private fun relaunch(name: String) {
        viewModelScope.launch {
            _loadState.emit(value = LoadState.Loaded(name = name))
        }
    }

    fun setEvent(event: LoadEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

}