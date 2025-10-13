package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {

    fun getTracksHistory(): List<Track>

    fun saveTracks(tracks: List<Track>)

    fun clearHistory()

    fun addTrackToHistory(newTrack: Track)
}