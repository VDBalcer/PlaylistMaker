package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackPlayerBinding
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerActivity : AppCompatActivity() {
    lateinit var playerInteractor: PlayerInteractor
    private lateinit var binding: TrackPlayerBinding
    private var mainThreadHandler: Handler? = null
    private val timerFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private lateinit var timerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrackPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>(TRACK)
        Glide.with(this)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, this)))
            .into(binding.trackArtworkImage)

        binding.trackTitle.text = track?.trackName
        binding.artistName.text = track?.artistName
        binding.trackTimeContent.text = track?.trackTime
        binding.trackAlbumContent.text = track?.collectionName

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
        binding.trackYearContent.text = formattedDate
        binding.trackGenreContent.text = track?.primaryGenreName
        binding.trackCountryContent.text = track?.country

        playerInteractor = Creator.getPlayerInteractor(track!!.previewUrl)
        timerRunnable = object : Runnable {
            override fun run() {
                binding.trackTimer.text = timerFormat.format(playerInteractor.getCurrentPosition())
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }
        binding.mainPlayerButton.setOnClickListener {
            playerInteractor.playbackControl()
            if (playerInteractor.isTrackPlaying()) {
                binding.mainPlayerButton.setImageResource(R.drawable.ic_stop)
            } else {
                binding.mainPlayerButton.setImageResource(R.drawable.ic_play)
            }
            mainThreadHandler?.post(timerRunnable)
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer()
        timerRunnable.let { mainThreadHandler?.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerRunnable.let { mainThreadHandler?.removeCallbacks(it) }
        playerInteractor.release()
    }

    companion object {
        const val ARTWORK_RADIUS = 8f
        const val TRACK = "track"
        const val DELAY = 250L
    }
}