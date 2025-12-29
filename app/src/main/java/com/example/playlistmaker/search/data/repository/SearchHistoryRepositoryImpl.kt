package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.settings.data.storage.StorageClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<List<Track>>,
    private val database: AppDatabase,
) : SearchHistoryRepository {

    override suspend fun addTrackToHistory(newTrack: Track) {
        val updated = getUpdatedTrackList(newTrack)
        saveTracks(updated)
    }

    override fun clearHistory() {
        storage.storeData(emptyList())
    }

    override suspend fun getTracksHistory(): List<Track> =
        withContext(Dispatchers.IO) {
            val tracks = storage.getData() ?: emptyList()
            val favoriteIdsSet =
                database.tracksDao().getTracksIds().first().toSet()
            tracks.map { track ->
                track.copy(
                    isFavorite = favoriteIdsSet.contains(track.trackId)
                )
            }
        }

    private suspend fun getUpdatedTrackList(newTrack: Track): MutableList<Track> {
        val tracks = getTracksHistory().toMutableList()
        val existingIndex = tracks.indexOfFirst { it.trackId == newTrack.trackId }
        if (existingIndex != -1) tracks.removeAt(existingIndex)
        tracks.add(0, newTrack)
        if (tracks.size > TRACKS_COUNT) tracks.subList(TRACKS_COUNT, tracks.size).clear()
        return tracks
    }

    override fun saveTracks(tracks: List<Track>) {
        storage.storeData(tracks)
    }

    companion object {
        const val TRACKS_COUNT = 10
    }
}