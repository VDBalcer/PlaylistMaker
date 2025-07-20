package com.example.playlistmaker.data

import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?
) {
    fun toDomain(): Track {
        return Track(
            trackName = trackName.orEmpty(),
            artistName = artistName.orEmpty(),
            trackTime = trackTimeMillis?.let {
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it)
            } ?: "",
            artworkUrl100 = artworkUrl100.orEmpty()
        )
    }
}
