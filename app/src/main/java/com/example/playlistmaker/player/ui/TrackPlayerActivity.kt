package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerActivity : AppCompatActivity() {
    private lateinit var binding: TrackPlayerBinding
    private lateinit var track: Track
    private val viewModel: TrackPlayerViewModel by viewModel {
        parametersOf(track.previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TrackPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra(TRACK)!!

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.apply {
            trackTitle.text = track.trackName
            artistName.text = track.artistName
            trackTimeContent.text = track.trackTime
            trackAlbumContent.text = track.collectionName
            trackYearContent.text = track.releaseDate.let { raw ->
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    .parse(raw)
                    ?.let { parsed ->
                        SimpleDateFormat("yyyy", Locale.getDefault()).format(parsed)
                    }
            }
            trackGenreContent.text = track.primaryGenreName
            trackCountryContent.text = track.country
        }
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, this)))
            .into(binding.trackArtworkImage)

        viewModel.observeProgressTime().observe(this) {
            binding.trackTimer.text = it
        }
        viewModel.observePlayerState().observe(this) {
            when (it) {
                TrackPlayerViewModel.STATE_PLAYING ->
                    binding.mainPlayerButton.setImageResource(R.drawable.ic_stop)

                else ->
                    binding.mainPlayerButton.setImageResource(R.drawable.ic_play)
            }
        }
        binding.mainPlayerButton.setOnClickListener {
            viewModel.playButtonClick()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    companion object {
        const val ARTWORK_RADIUS = 8f
        const val TRACK = "track"
    }
}