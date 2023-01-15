package com.example.songchimp.Service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.songchimp.R
import com.example.songchimp.UI.Fragments.SongControllerFragment

class MyServiceSong : Service() {
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val binder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MyServiceSong = this@MyServiceSong
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationSong()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var url = intent.getStringExtra("song_url").toString()
//        var position = intent.getStringExtra("position")
//        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()

        showNotification(intent)

        if (!mediaPlayer.isPlaying) {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
        } else if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    fun createNotificationSong() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "songChannelId",
                "My Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }

    @SuppressLint("NewApi")
    fun showNotification(intent: Intent) {
        var title = intent.getStringExtra("song_title")
        var author = intent.getStringExtra("song_author")
        val notification = NotificationCompat.Builder(this, "songChannelId")
            .setContentTitle(title)
            .setContentText(author)
            .setSmallIcon(R.drawable.ic_icon_app)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_last, "last", null)
            .addAction(R.drawable.ic_stop, "PS", null)
            .addAction(R.drawable.ic_next, "next", null)
            .build()

        startForeground(1, notification)
    }

    fun changeSongPosition(intent: Intent) {
        val position = intent.getStringExtra("song_position").toString()
        Log.d("position", "the position is $position")
    }
}