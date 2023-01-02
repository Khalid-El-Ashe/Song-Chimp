package com.example.songchimp.Listener

import com.example.songchimp.Entities.Song
import com.example.songchimp.ViewModel.SongViewModel

interface OnSongClickListener {
    fun onSongClick(song: Song, position: Int)
}