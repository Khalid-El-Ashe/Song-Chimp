package com.example.songchimp.Repository


import androidx.lifecycle.LiveData
import com.example.songchimp.Dao.SongDao
import com.example.songchimp.Entities.Song

class SongRepository(val dao: SongDao) {

    suspend fun insertSong(songs: Song) {
        return dao.insertSongs(songs)
    }

    fun selectAllFavSongs(): LiveData<List<Song>> {
        return dao.selectAllFavSongs()
    }

    suspend fun deleteSongFav(songs: Song){
        return dao.deleteSongFav(songs)
    }
}
