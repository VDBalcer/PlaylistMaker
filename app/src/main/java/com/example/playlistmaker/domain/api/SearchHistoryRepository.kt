package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {

    fun addTrackToHistory(newTrack: Track)

    fun clearHistory()

    fun getTracksHistory(): List<Track>

    fun saveTracksToPrefs(tracks: List<Track>)

    fun getUpdatedTrackList(newTrack: Track): MutableList<Track>
}