package com.zut.localcrafts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Artisan::class, Product::class],
    version = 2,
    exportSchema = false
)
abstract class LocalCraftsDatabase : RoomDatabase() {

    abstract fun artisanDao(): ArtisanDao

    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: LocalCraftsDatabase? = null

        fun getDatabase(context: Context): LocalCraftsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalCraftsDatabase::class.java,
                    "localcrafts_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}