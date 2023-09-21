package com.tocaboca.tocacar.presentation.memory

import androidx.annotation.DrawableRes

data class PuzzleBlock(
    val id: Int,
    @DrawableRes val image: Int,
    var isVisible: Boolean = true
)
