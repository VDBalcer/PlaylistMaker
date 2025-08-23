package com.example.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.search.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_player)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>("track")
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

    }

    companion object {
        const val ARTWORK_RADIUS = 8f
    }
}