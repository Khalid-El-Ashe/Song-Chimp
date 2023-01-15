package com.example.songchimp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.songchimp.Dao.PlayListDao
import com.example.songchimp.Database.SongDatabase
import com.example.songchimp.Entities.PlayList
import com.example.songchimp.Entities.Song
import com.example.songchimp.Repository.PlayListRepository
import com.example.songchimp.Repository.SongRepository
import kotlinx.coroutines.launch

class PlayListViewModel(application: Application) : AndroidViewModel(application) {
    val playListRepository: PlayListRepository

    init {
        val dao = SongDatabase.databaseInstance(application).playListDao()
        playListRepository = PlayListRepository(dao)
    }

    fun insertPlayList(playList: PlayList) = viewModelScope.launch {
        playListRepository.insertPlayList(playList)
    }

    fun selectPlayList(): LiveData<List<PlayList>> = playListRepository.selectPlayList()
//    fun selectPlayListSong(id: Int): LiveData<List<Song>> = playListRepository.selectPlayListSong(id)

//    fun insertSongToPlayList(id: Int) = viewModelScope.launch {
//        playListRepository.insertSongToPlayList(id)
//    }
}