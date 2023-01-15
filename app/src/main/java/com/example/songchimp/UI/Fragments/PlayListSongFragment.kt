package com.example.songchimp.UI.Fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.songchimp.Adapters.SongAdapter
import com.example.songchimp.Entities.Song
import com.example.songchimp.Functions.setupBottomSheetDialog
import com.example.songchimp.Listener.OnSongClickListener
import com.example.songchimp.R
import com.example.songchimp.ViewModel.SongViewModel
import com.example.songchimp.databinding.FragmentPlayListSongBinding

class PlayListSongFragment : Fragment(R.layout.fragment_play_list_song), OnSongClickListener {
    private lateinit var binding: FragmentPlayListSongBinding
    private lateinit var adapter: SongAdapter
    private var songsList = mutableListOf<Song>()
    private var mp: MediaPlayer = MediaPlayer()
    lateinit var viewModel: SongViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListSongBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRecyclerView()
    }

    private fun getRecyclerView() {
        adapter = SongAdapter(requireContext(), songsList,  this)
        binding.rvPlayListSong.adapter = adapter
        binding.rvPlayListSong.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onSongClick(song: Song, position: Int) {
        setupBottomSheetDialog(mp, songsList, position, viewModel)
    }
}