package com.tocaboca.tocacar.presentation.memory

data class MemoryCard(
    val id: Int,
    var facedUp: Boolean = false,
    var matched: Boolean = false
)