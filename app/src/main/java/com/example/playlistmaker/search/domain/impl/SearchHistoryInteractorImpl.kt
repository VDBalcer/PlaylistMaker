package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override suspend fun addTrackToHistory(newTrack: Track) {
        repository.addTrackToHistory(newTrack)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override suspend fun getTracksHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getTracksHistory())
    }

    override fun saveTracksToPrefs(tracks: List<Track>) {
        repository.saveTracks(tracks)
    }
}