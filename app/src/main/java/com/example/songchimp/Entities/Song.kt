package com.example.songchimp.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_table")
class Song: java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    var song_id: Int? = null
    var song_title: String?
    var song_url: String?
    var song_author: String?

    constructor(song_title: String?, song_url: String?, song_author: String?) {
        this.song_title = song_title
        this.song_url = song_url
        this.song_author = song_author
    }
}