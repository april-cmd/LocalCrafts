package com.zut.localcrafts.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtisanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtisan(artisan: Artisan)

    @Query("SELECT * FROM artisans")
    fun getAllArtisans(): Flow<List<Artisan>>

    @Query("SELECT * FROM artisans WHERE artisanId = :artisanId")
    suspend fun getArtisanById(artisanId: String): Artisan?
}