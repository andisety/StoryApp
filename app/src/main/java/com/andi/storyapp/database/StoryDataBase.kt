package com.andi.storyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andi.storyapp.network.StoryResponeItem

@Database(
    entities = [StoryResponeItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDataBase:RoomDatabase() {
    abstract fun storyDao():StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDataBase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDataBase::class.java, "story_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}