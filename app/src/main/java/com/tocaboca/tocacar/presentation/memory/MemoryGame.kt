package com.tocaboca.tocacar.presentation.memory

import com.tocaboca.tocacar.R

class MemoryGame {

    private var numPairsFound = 0

    var memoryCards = listOf<MemoryCard>(
        MemoryCard(id = R.drawable.diamonds_card),
        MemoryCard(id = R.drawable.diamonds_card),
        MemoryCard(id = R.drawable.diamonds_card),
        MemoryCard(id = R.drawable.diamonds_card),
        MemoryCard(id = R.drawable.clubs_card),
        MemoryCard(id = R.drawable.clubs_card),
        MemoryCard(id = R.drawable.clubs_card),
        MemoryCard(id = R.drawable.clubs_card),
        MemoryCard(id = R.drawable.hearts_card),
        MemoryCard(id = R.drawable.hearts_card),
        MemoryCard(id = R.drawable.hearts_card),
        MemoryCard(id = R.drawable.hearts_card),
    )

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val randomizedImages = memoryCards.shuffled()
        memoryCards = randomizedImages
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card: MemoryCard = memoryCards[position]
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.facedUp = !card.facedUp
        return foundMatch

    }

    fun wonGame(): Boolean {
        return numPairsFound == 6
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (memoryCards[position1].id != memoryCards[position2].id) {
            return false
        }
        memoryCards[position1].matched = true
        memoryCards[position2].matched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card: MemoryCard in memoryCards) {
            if (!card.matched) {
                card.facedUp = false
            }
        }
    }

    fun isCardFacedUp(position: Int): Boolean {
        return memoryCards[position].facedUp
    }


    fun restore() {
        memoryCards.shuffled()

        numPairsFound = 0
        memoryCards.forEach {
            it.facedUp = false
            it.matched = false
        }
    }

}