package com.example.playlistmaker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.mediaPlayer.Player
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.search.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerActivity : AppCompatActivity() {
    lateinit var player: Player
    private var mainThreadHandler: Handler? = null
    private val timerFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var timerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_player)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>(TRACK)
        Glide.with(this)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, this)))
            .into(findViewById<AppCompatImageView>(R.id.track_artwork_image))

        findViewById<AppCompatTextView>(R.id.track_title).text = track?.trackName
        findViewById<AppCompatTextView>(R.id.artist_name).text = track?.artistName
        findViewById<AppCompatTextView>(R.id.track_time_content).text = track?.trackTime
        findViewById<AppCompatTextView>(R.id.track_album_content).text = track?.collectionName

        val releaseDate = track?.releaseDate?.let {
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            ).parse(it)
        }
        val formattedDate = releaseDate?.let {
            SimpleDateFormat("yyyy", Locale.getDefault()).format(
                it
            )
        }
        findViewById<AppCompatTextView>(R.id.track_year_content).text = formattedDate

        findViewById<AppCompatTextView>(R.id.track_genre_content).text = track?.primaryGenreName
        findViewById<AppCompatTextView>(R.id.track_country_content).text = track?.country

        val playButton = findViewById<ImageButton>(R.id.main_player_button)
        player = Player(track!!.previewUrl, playButton)
        val trackTimer = findViewById<AppCompatTextView>(R.id.track_timer)
        timerRunnable = object: Runnable {
            override fun run() {
                trackTimer.text = timerFormat.format(player.getCurrentPosition())
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }
        playButton.setOnClickListener {
            player.playbackControl()
            mainThreadHandler?.post(timerRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        player.pausePlayer()
        timerRunnable.let { mainThreadHandler?.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerRunnable.let { mainThreadHandler?.removeCallbacks(it) }
        player.release()
    }

    companion object {
        const val ARTWORK_RADIUS = 8f
        const val TRACK = "track"
        const val DELAY = 250L
    }
}