package com.tocaboca.tocacar.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tocaboca.tocacar.data.NeonCardRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

sealed interface NeonCardEvent {
    data class OnCardFlipped(val name: String) : NeonCardEvent
}

class NeonCardViewModel(
    private val neonCardRepository: NeonCardRepository
) : ViewModel() {

    private val events = MutableSharedFlow<NeonCardEvent>()

    init {
        viewModelScope.launch {
            events.collect {
                if (it is NeonCardEvent.OnCardFlipped) {
                    saveLocally(it.name)
                }
            }
        }
    }

    fun setEvent(event: NeonCardEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    private fun saveLocally(name: String) {
        viewModelScope.launch {
            neonCardRepository.insert(name)
        }
    }
}