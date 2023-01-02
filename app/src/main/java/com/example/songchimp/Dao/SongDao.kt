package com.example.songchimp.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.songchimp.Entities.Song

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: Song)

    @Query("select * from song_table")
    fun selectAllFavSongs(): LiveData<List<Song>>

    @Delete
    suspend fun deleteSongFav(songs: Song)
}