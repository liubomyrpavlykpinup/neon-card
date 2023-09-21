package com.tocaboca.tocacar.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NeonCardRepository(database: NeonCardDatabase) {

    private val neonCardDao = database.neonCardDao()

    suspend fun neonCard() = withContext(Dispatchers.IO) {
        return@withContext neonCardDao.neonCard()
    }

    suspend fun insert(name: String) {
        withContext(Dispatchers.IO) {
            neonCardDao.insert(NeonCard(0, name, System.currentTimeMillis()))
        }
    }
}