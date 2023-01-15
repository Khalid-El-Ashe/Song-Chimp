package com.example.songchimp.UI.Fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.songchimp.R
import com.example.songchimp.Service.MyServiceSong
import com.example.songchimp.ViewModel.SongViewModel
import com.example.songchimp.databinding.FragmentSongControllerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SongControllerFragment : Fragment(R.layout.fragment_song_controller) {
    private lateinit var binding: FragmentSongControllerBinding
    private lateinit var viewModel: SongViewModel
    private lateinit var myServiceSong: MyServiceSong

    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val songData by navArgs<SongControllerFragmentArgs>()

    private var check: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongControllerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = SongViewModel(requireActivity().application)
        myServiceSong = MyServiceSong()

        binding.tvMediaSongTitle.setText(songData.songs.song_title)

        binding.imgPS.setOnClickListener {

            if (check == 0) {
                check = 1
                binding.imgPS.setImageResource(R.drawable.ic_start)
                val intent = Intent(requireContext(), MyServiceSong::class.java)
                intent.putExtra("song_title", songData.songs.song_title)
                intent.putExtra("song_author", songData.songs.song_author)
                intent.putExtra("song_url", songData.songs.song_url)
                intent.putExtra("position",songData.position)
                requireActivity().startService(intent)
            } else if (check == 1) {
                check = 0
                binding.imgPS.setImageResource(R.drawable.ic_stop)
                val intent = Intent(requireContext(), MyServiceSong::class.java)
                requireActivity().stopService(intent)
            }
        }

        binding.imgFav.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.insertSong(songData.songs)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "success add to favorite", Toast.LENGTH_SHORT)
                        .show()
                    binding.imgFav.setImageResource(R.drawable.ic_fav_success)
                }
            }
        }

        binding.imgRepeat.setOnClickListener {
            if (!mediaPlayer.isLooping) {
                mediaPlayer.isLooping = true
                binding.imgRepeat.setImageResource(R.drawable.ic_repeat_one)
            } else {
                mediaPlayer.isLooping = false
                binding.imgRepeat.setImageResource(R.drawable.ic_repeat)
            }
        }

        binding.imgNext.setOnClickListener {
//            val intent = Intent(requireContext(), MyServiceSong::class.java)
//
//            val myServiceSong = MyServiceSong()
//            myServiceSong.nextSongPosition(intent)
        }
        binding.imgLast.setOnClickListener {
//            myServiceSong.lastSongPosition(position = songData.position)
        }
    }

    private fun durationConverter(duration: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration
                )
            )
        )
    }

//    private fun displaySongImage() {
//        val mediaMetadataRetriever = MediaMetadataRetriever()
//        mediaMetadataRetriever.setDataSource(songData.song.song_url)
//        val data = mediaMetadataRetriever.embeddedPicture
//        if (data != null) {
//            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
//        }
//    }

}