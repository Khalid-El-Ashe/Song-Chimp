package com.example.songchimp.Functions

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.songchimp.Entities.Song
import com.example.songchimp.R
import com.example.songchimp.ViewModel.SongViewModel
import com.example.songchimp.databinding.SongSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

fun Fragment.setupBottomSheetDialog(
    mediaPlayer: MediaPlayer,
    songsList: MutableList<Song>,
    position: Int,
    viewModel: SongViewModel
) {

    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.song_sheet, null)
    dialog.setContentView(view)
    val binding = SongSheetBinding.bind(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    var currentPosition = position

    var runnable: Runnable = kotlinx.coroutines.Runnable { }

    val timerSong = view.findViewById<TextView>(R.id.tvTimerSong)

    var song = songsList[currentPosition]

    binding.imgPS.setOnClickListener {
        if (!mediaPlayer.isPlaying) {
            binding.imgPS.setImageResource(R.drawable.ic_start)
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(song.song_url)
                mediaPlayer.prepare()
                mediaPlayer.start()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "have a problem : $e", Toast.LENGTH_SHORT).show()
            }
        } else if (mediaPlayer.isPlaying) {
            binding.imgPS.setImageResource(R.drawable.ic_stop)
            mediaPlayer.pause()
        }
    }

    binding.seekBarSong.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if (p2) {
                mediaPlayer.seekTo(p1)
                timerSong.text = durationConverter(p1.toLong())
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}

        override fun onStopTrackingTouch(p0: SeekBar?) {
            if (mediaPlayer.isPlaying) {
                if (binding.seekBarSong != null) {
                    mediaPlayer.seekTo(binding.seekBarSong.progress)
                }
            }
        }
    })

    mediaPlayer.setOnSeekCompleteListener { }

    runnable = Runnable {
        Log.e("TAG", "setupBottomSheetDialog: Runnable check")
        if (mediaPlayer.isPlaying) {
            timerSong.text = durationConverter(mediaPlayer.currentPosition.toLong())
            binding.seekBarSong.progress = mediaPlayer.currentPosition
            binding.seekBarSong.max = mediaPlayer.duration
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
    }
    Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)

    binding.imgRepeat.setOnClickListener {
        if (!mediaPlayer.isLooping) {
            mediaPlayer.isLooping = true
            binding.imgRepeat.setImageResource(R.drawable.ic_repeat_one)
        } else {
            mediaPlayer.isLooping = false
            binding.imgRepeat.setImageResource(R.drawable.ic_repeat)
        }
    }

    binding.imgFav.setOnClickListener {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.insertSong(song)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "success add to favorite", Toast.LENGTH_SHORT)
                    .show()
                binding.imgFav.setImageResource(R.drawable.ic_fav_success)
            }
        }
    }

    binding.songDialogTitle.text = song.song_title

    binding.imgNext.setOnClickListener {
        if (currentPosition < songsList.size - 1) {
            currentPosition = currentPosition + 1
            try {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songsList.get(currentPosition).song_url.toString())
                mediaPlayer.prepare()
                mediaPlayer.start()
                binding.songDialogTitle.text = songsList.get(currentPosition).song_title
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "have a problem $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    binding.imgLast.setOnClickListener {
        if (currentPosition < songsList.size - 1) {
            currentPosition = currentPosition - 1
            try {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songsList.get(currentPosition).song_url.toString())
                mediaPlayer.prepare()
                mediaPlayer.start()
                binding.songDialogTitle.text = songsList.get(currentPosition).song_title
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "have a problem $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun durationConverter(duration: Long): String {
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


