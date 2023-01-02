package com.example.songchimp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.songchimp.Entities.PlayList
import com.example.songchimp.databinding.ItemPlayListBinding

class PlayListAdapter(var plays: MutableList<PlayList>) :
    RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {

    class PlayListViewHolder(var binding: ItemPlayListBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(
            ItemPlayListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        var playList = plays[position]
        holder.binding.tvPlayTitleItem.text = playList.playListName
        holder.binding.tvPlayListSize.text = "size"
    }

    override fun getItemCount(): Int = plays.size

    fun getPlayLists(playLists: List<PlayList>) {
        this.plays.addAll(playLists)
        notifyDataSetChanged()
    }
}