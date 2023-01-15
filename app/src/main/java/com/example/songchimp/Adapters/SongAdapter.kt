package com.example.songchimp.Adapters

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.songchimp.Entities.Song
import com.example.songchimp.databinding.ItemMusicBinding
import com.example.songchimp.Listener.OnSongClickListener

class SongAdapter(
    var context: Context,
    var songList: MutableList<Song>,
    private val songClick: OnSongClickListener,
) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    fun filtering(filterList: ArrayList<Song>) {
        songList = filterList
        notifyDataSetChanged()
    }

    class SongViewHolder(var binding: ItemMusicBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        var song = songList[position]

        holder.binding.songTitle.text = song.song_title
        holder.binding.songAuthor.text = song.song_author


        holder.binding.itemContainerSong.setOnClickListener {
            songClick.onSongClick(song, position)
        }

        holder.itemView.setOnLongClickListener {
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = songList.size

    fun getSongs(songs: List<Song>){
        this.songList.addAll(songs)
        notifyDataSetChanged()
    }

}