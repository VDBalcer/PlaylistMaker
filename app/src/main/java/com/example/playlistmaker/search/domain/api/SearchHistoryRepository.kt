package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {

    fun addTrackToHistory(newTrack: Track)

    fun clearHistory()

    fun getTracksHistory(): List<Track>

    fun saveTracksToPrefs(tracks: List<Track>)

    fun getUpdatedTrackList(newTrack: Track): MutableList<Track>
}