package com.example.keepup.data.db

import android.content.Context
import androidx.room.*
import com.example.keepup.data.model.NewsDataItem

@Database(
    entities = [NewsDataItem::class],
    version = 2
)

@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getNewsItemDao(): NewsItemDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }

        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_db.db"
            ).fallbackToDestructiveMigration().build()

    }
}