package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.data.storage.PrefsStorageClient

class SearchHistoryRepositoryImpl(
    private val storage: PrefsStorageClient<List<Track>>
) : SearchHistoryRepository {

    override fun addTrackToHistory(newTrack: Track) {
        val updated = getUpdatedTrackList(newTrack)
        saveTracks(updated)
    }

    override fun clearHistory() {
        storage.storeData(emptyList())
    }

    override fun getTracksHistory(): List<Track> {
        return storage.getData() ?: emptyList()
    }

    override fun saveTracks(tracks: List<Track>) {
        storage.storeData(tracks)
    }

    private fun getUpdatedTrackList(newTrack: Track): MutableList<Track> {
        val tracks = getTracksHistory().toMutableList()
        val existingIndex = tracks.indexOfFirst { it.trackId == newTrack.trackId }
        if (existingIndex != -1) tracks.removeAt(existingIndex)
        tracks.add(0, newTrack)
        if (tracks.size > TRACKS_COUNT) tracks.subList(TRACKS_COUNT, tracks.size).clear()
        return tracks
    }

    companion object {
        const val TRACKS_COUNT = 10
    }
}