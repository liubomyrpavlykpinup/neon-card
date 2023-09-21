package com.tocaboca.tocacar.data

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GoogleIdentifierReceiver {
        
    suspend fun get(context: Context): String = withContext(Dispatchers.IO) {
        return@withContext AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
    }
}