package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}