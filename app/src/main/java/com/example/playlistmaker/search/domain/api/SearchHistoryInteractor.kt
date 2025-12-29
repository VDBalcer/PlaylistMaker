package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryInteractor {

    suspend fun addTrackToHistory(newTrack: Track)

    fun clearHistory()

    suspend fun getTracksHistory(consumer: HistoryConsumer)

    fun saveTracksToPrefs(tracks: List<Track>)

    interface HistoryConsumer {
        fun consume(historyTracks: List<Track>)
    }
}