package com.example.songchimp.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.songchimp.Entities.PlayList
import com.example.songchimp.Entities.Song

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlayList)

    @Query("select * from play_list_table")
    fun selectPlayList(): LiveData<List<PlayList>>

//    @Query("select * from play_list_table where songId =:id")
//    fun selectPlayListSong(id: Int): LiveData<List<Song>>

//    @Insert
//    suspend fun insertSongToPlayList(id: Int)
}