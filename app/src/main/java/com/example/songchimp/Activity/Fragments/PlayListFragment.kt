package com.example.songchimp.Activity.Fragments

import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.songchimp.Adapters.PlayListAdapter
import com.example.songchimp.Entities.PlayList
import com.example.songchimp.Entities.Song
import com.example.songchimp.R
import com.example.songchimp.ViewModel.PlayListViewModel
import com.example.songchimp.databinding.FragmentPlayListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayListFragment : Fragment(R.layout.fragment_play_list) {
    private lateinit var binding: FragmentPlayListBinding
    lateinit var viewModel: PlayListViewModel
    private lateinit var adapter: PlayListAdapter
    private var playLists = mutableListOf<PlayList>()
    private var songs = mutableListOf<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = PlayListViewModel(requireActivity().application)

        binding.imgAddPlayList.setOnClickListener {
            binding.editTitlePlayList.visibility = View.VISIBLE
            binding.imgPlayListSave.visibility = View.VISIBLE
        }

        binding.imgPlayListSave.setOnClickListener {
            if (TextUtils.isEmpty(binding.editTitlePlayList.text.toString())){
                binding.editTitlePlayList.error = "the title is require"
            } else {
                var title = binding.editTitlePlayList.text.toString()
                val playList = PlayList(title)

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertPlayList(playList)
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "success add play list", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.editTitlePlayList.clearFocus()
                binding.editTitlePlayList.visibility = View.GONE
                binding.imgPlayListSave.visibility = View.GONE
            }
        }

        getRecyclerViewItems()
    }

    private fun getRecyclerViewItems() {
        adapter = PlayListAdapter(playLists)
        binding.rvPlaylistsMusic.adapter = adapter
        val gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvPlaylistsMusic.layoutManager = gridLayoutManager
        viewModel.selectPlayList().observe(viewLifecycleOwner, Observer { playsList ->
            adapter.getPlayLists(playsList)
        })
    }
}