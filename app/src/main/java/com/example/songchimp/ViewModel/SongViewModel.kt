package com.example.songchimp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.songchimp.Database.SongDatabase
import com.example.songchimp.Entities.Song
import com.example.songchimp.Repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {
    val songRepository: SongRepository

    init {
        val dao = SongDatabase.databaseInstance(application).songDao()
        songRepository = SongRepository(dao)
    }

     suspend fun insertSong(songs: Song) = viewModelScope.launch {
        songRepository.insertSong(songs)
    }

    fun selectAllFavSongs(): LiveData<List<Song>> = songRepository.selectAllFavSongs()
     fun deleteSongFav(songs: Song) = viewModelScope.launch {
        songRepository.deleteSongFav(songs)
    }
}