package com.example.songchimp.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_list_table")
class PlayList{

    @PrimaryKey(autoGenerate = true)
    var playListId: Int? = null
    var playListName: String?

    constructor(playListName: String?) {
        this.playListName = playListName
    }
}