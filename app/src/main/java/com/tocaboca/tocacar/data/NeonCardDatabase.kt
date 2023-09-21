package com.tocaboca.tocacar.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class NeonCard(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val timestamp: Long,
)

@Dao
interface NeonCardDao {

    @Query("SELECT * FROM NeonCard ORDER BY timestamp DESC LIMIT 1")
    suspend fun neonCard(): NeonCard?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planeInfo: NeonCard)

}

@Database(entities = [NeonCard::class], version = 1)
abstract class NeonCardDatabase : RoomDatabase() {

    abstract fun neonCardDao(): NeonCardDao

    companion object {

        fun getInstance(appContext: Context) =
            Room.databaseBuilder(appContext, NeonCardDatabase::class.java, "neon-cards-db").build()
    }
}