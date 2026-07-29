package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProjectEntity::class, BrandKitEntity::class, TemplateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AdVestDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun brandKitDao(): BrandKitDao
    abstract fun templateDao(): TemplateDao

    companion object {
        @Volatile
        private var INSTANCE: AdVestDatabase? = null

        fun getDatabase(context: Context): AdVestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AdVestDatabase::class.java,
                    "advest_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
