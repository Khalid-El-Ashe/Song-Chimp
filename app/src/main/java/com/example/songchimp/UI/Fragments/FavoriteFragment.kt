package com.example.songchimp.UI.Fragments

import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.songchimp.Adapters.SongAdapter
import com.example.songchimp.Entities.Song
import com.example.songchimp.Listener.OnSongClickListener
import com.example.songchimp.R
import com.example.songchimp.ViewModel.SongViewModel
import com.example.songchimp.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(R.layout.fragment_favorite), OnSongClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    private var songs = mutableListOf<Song>()
    lateinit var viewModel: SongViewModel
    private lateinit var adapter: SongAdapter
    private var mp: MediaPlayer = MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = SongViewModel(requireActivity().application)

        getRecyclerViewItems()

        // this function to make swiping for recycler view
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val song = adapter.songList.get(position)
                askToDelete(song) // this function to ask delete or not

            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvFavMusic)
        }
    }

    private fun getRecyclerViewItems() {
        adapter = SongAdapter(requireContext(), songs,  this)
        binding.rvFavMusic.adapter = adapter
        binding.rvFavMusic.layoutManager = LinearLayoutManager(requireContext())
        viewModel.selectAllFavSongs().observe(viewLifecycleOwner, Observer { songs ->
            adapter.getSongs(songs)
        })
    }

    override fun onSongClick(song: Song, position: Int) {
        val action =
            FavoriteFragmentDirections.actionFavoriteFragmentToFragmentSongController2(song,position)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun askToDelete(song: Song) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.drawable.ic_icon_app)
        builder.setMessage("Do you need deleted from favorite")
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteSongFav(song)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "success delete", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
        })
        builder.create()
        builder.show()
    }
}