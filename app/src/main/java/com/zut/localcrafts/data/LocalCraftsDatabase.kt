package com.zut.localcrafts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Artisan::class, Product::class],
    version = 3,
    exportSchema = false
)
abstract class LocalCraftsDatabase : RoomDatabase() {

    abstract fun artisanDao(): ArtisanDao

    abstract fun productDao(): ProductDao

    companion object {

        // Adds login (password hash + salt) and craft speciality to artisans, keeping existing data
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE artisans ADD COLUMN speciality TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE artisans ADD COLUMN passwordHash TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE artisans ADD COLUMN passwordSalt TEXT NOT NULL DEFAULT ''")
            }
        }

        @Volatile
        private var INSTANCE: LocalCraftsDatabase? = null

        fun getDatabase(context: Context): LocalCraftsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalCraftsDatabase::class.java,
                    "localcrafts_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}