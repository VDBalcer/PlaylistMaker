package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {

    suspend fun getTracksHistory(): List<Track>

    fun saveTracks(tracks: List<Track>)

    fun clearHistory()

    suspend fun addTrackToHistory(newTrack: Track)
}