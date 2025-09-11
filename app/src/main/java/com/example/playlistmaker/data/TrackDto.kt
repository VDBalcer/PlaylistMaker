package com.example.playlistmaker.data

import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto(
    val trackId: Int?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) {
    fun toDomain(): Track {
        return Track(
            trackId = trackId ?: 0,
            trackName = trackName.orEmpty(),
            artistName = artistName.orEmpty(),
            trackTime = trackTimeMillis?.let {
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it)
            } ?: "",
            artworkUrl100 = artworkUrl100.orEmpty(),
            collectionName = collectionName.orEmpty(),
            releaseDate = releaseDate.orEmpty(),
            primaryGenreName = primaryGenreName.orEmpty(),
            country = country.orEmpty(),
            previewUrl = previewUrl.orEmpty()
        )
    }
}
