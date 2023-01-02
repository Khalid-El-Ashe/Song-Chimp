package com.example.songchimp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.songchimp.Dao.PlayListDao
import com.example.songchimp.Dao.SongDao
import com.example.songchimp.Entities.PlayList
import com.example.songchimp.Entities.Song

@Database(entities = [Song::class, PlayList::class], version = 2)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playListDao(): PlayListDao

    companion object {
        @Volatile
        var instance: SongDatabase? = null

        fun databaseInstance(context: Context): SongDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val roomDatabaseInstance =
                    Room.databaseBuilder(context, SongDatabase::class.java, "song_database")
                        .allowMainThreadQueries().build()
                instance = roomDatabaseInstance

                return roomDatabaseInstance
            }
        }
    }
}