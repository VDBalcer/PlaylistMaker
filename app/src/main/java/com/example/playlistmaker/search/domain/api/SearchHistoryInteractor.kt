package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryInteractor {

    fun addTrackToHistory(newTrack: Track)

    fun clearHistory()

    fun getTracksHistory(consumer: HistoryConsumer)

    fun saveTracksToPrefs(tracks: List<Track>)

    interface HistoryConsumer {
        fun consume(historyTracks: List<Track>)
    }
}