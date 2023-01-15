package com.example.songchimp.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "play_list_table", foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["song_id"],
            childColumns = ["songId"]
        )
    ]
)
class PlayList {

    @PrimaryKey(autoGenerate = true)
    var playListId: Int? = null
    var playListName: String?
    var songId: Int? = null

    constructor(playListName: String?) {
        this.playListName = playListName
    }
}