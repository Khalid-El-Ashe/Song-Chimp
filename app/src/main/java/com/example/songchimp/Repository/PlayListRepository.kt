package com.example.songchimp.Repository

import androidx.lifecycle.LiveData
import com.example.songchimp.Dao.PlayListDao
import com.example.songchimp.Entities.PlayList

class PlayListRepository(val playListDao: PlayListDao) {

    suspend fun insertPlayList(playList: PlayList) {
         playListDao.insertPlayList(playList)
    }

    fun selectPlayList(): LiveData<List<PlayList>> {
        return playListDao.selectPlayList()
    }
}