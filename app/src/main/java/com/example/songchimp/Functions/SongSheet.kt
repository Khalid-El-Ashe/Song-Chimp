package com.example.songchimp.Functions

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.songchimp.Entities.Song
import com.example.songchimp.R
import com.example.songchimp.ViewModel.SongViewModel
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
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    var currentPosition = position

    var runnable: Runnable = kotlinx.coroutines.Runnable { }

    val imgStart = view.findViewById<ImageView>(R.id.imgP_S)
    val imgLast = view.findViewById<ImageView>(R.id.imgLast)
    val imgNext = view.findViewById<ImageView>(R.id.imgNext)
    val seekBar = view.findViewById<SeekBar>(R.id.seekBarSong)
    val title = view.findViewById<TextView>(R.id.song_dialog_title)
    val imgRepeat = view.findViewById<ImageView>(R.id.imgRepeat)
    val imgFav = view.findViewById<ImageView>(R.id.imgFav)

    val timerSong = view.findViewById<TextView>(R.id.tvTimerSong)

    var song = songsList[currentPosition]

    imgStart.setOnClickListener {

        if (!mediaPlayer.isPlaying) {
            imgStart.setImageResource(R.drawable.ic_start)
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(song.song_url)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "have a problem : $e", Toast.LENGTH_SHORT).show()
            }
        } else if (mediaPlayer.isPlaying) {
            imgStart.setImageResource(R.drawable.ic_stop)
            mediaPlayer.pause()
        }
    }

    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if (p2) {
                mediaPlayer.seekTo(p1)
                timerSong.text = durationConverter(p1.toLong())
            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}

        override fun onStopTrackingTouch(p0: SeekBar?) {
            if (mediaPlayer.isPlaying) {
                if (seekBar != null) {
                    mediaPlayer.seekTo(seekBar.progress)
                }
            }
        }
    })

    runnable = Runnable {

        if (mediaPlayer.isPlaying) {
            timerSong.text = durationConverter(mediaPlayer.currentPosition.toLong())
            seekBar.progress = mediaPlayer.currentPosition
            seekBar.max = mediaPlayer.duration
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
    }
    Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)

    imgRepeat.setOnClickListener {
        if (!mediaPlayer.isLooping) {
            mediaPlayer.isLooping = true
            imgRepeat.setImageResource(R.drawable.ic_repeat_one)
        } else {
            mediaPlayer.isLooping = false
            imgRepeat.setImageResource(R.drawable.ic_repeat)
        }
    }

    imgFav.setOnClickListener {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.insertSong(song)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "success add to favorite", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    title.text = song.song_title

    imgNext.setOnClickListener {
        if (currentPosition < songsList.size - 1) {
            currentPosition = currentPosition + 1
            try {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songsList.get(currentPosition).song_url)
                mediaPlayer.prepare()
                mediaPlayer.start()
                title.text = songsList.get(currentPosition).song_title
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "have a problem $e", Toast.LENGTH_SHORT).show()
            }
        } else {
            currentPosition = 0
        }
    }

    imgLast.setOnClickListener {
        if (currentPosition < songsList.size - 1) {
            currentPosition = currentPosition - 1
            try {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songsList.get(currentPosition).song_url)
                mediaPlayer.prepare()
                mediaPlayer.start()
                title.text = songsList.get(currentPosition).song_title
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "have a problem $e", Toast.LENGTH_SHORT).show()
            }
        } else {
            currentPosition = 0
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


