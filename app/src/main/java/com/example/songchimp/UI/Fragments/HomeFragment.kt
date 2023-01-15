package com.example.songchimp.UI.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.songchimp.Adapters.SongAdapter
import com.example.songchimp.Entities.Song
import com.example.songchimp.Listener.OnSongClickListener
import com.example.songchimp.R
import com.example.songchimp.Service.MyServiceSong
import com.example.songchimp.ViewModel.SongViewModel
import com.example.songchimp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home), OnSongClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val REQUEST_CODE = 4

    private var listSongs = mutableListOf<Song>()

    private lateinit var adapter: SongAdapter
//    var mp: MediaPlayer = MediaPlayer()

    private lateinit var viewModel: SongViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = SongViewModel(requireActivity().application)

        // check permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            readSongs()
        }

        binding.songHomeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                searchFiltering(p0)
                return true
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) readSongs()
    }

    @SuppressLint("Range") // this for enable the exception to line of 80..82
    private fun readSongs() {
        var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        var rs = requireContext().contentResolver.query(uri, null, selection, null, null)
        if (rs != null) {
            while (rs.moveToNext()) {
                var url = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DATA))
                var title = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                var author = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.ARTIST))

                listSongs.add(Song(title, url, author))
            }
        }

        adapter = SongAdapter(requireContext(), listSongs, this)
        binding.rvMusic.adapter = adapter
        binding.rvMusic.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onSongClick(song: Song, position: Int) {
//        setupBottomSheetDialog(mp, listSongs, position, viewModel)
        val action = HomeFragmentDirections.actionHomeFragmentToFragmentSongController(song,position)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun searchFiltering(search: String) {
        val filterList = arrayListOf<Song>()
        for (s in listSongs) {
            if (s.song_title.toString().contains(search)) {
                filterList.add(s)
            }
        }
        adapter.filtering(filterList)
    }
}