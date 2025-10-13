package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import java.util.concurrent.Executors

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun addTrackToHistory(newTrack: Track) {
        executor.execute {
            repository.addTrackToHistory(newTrack)
        }
    }

    override fun clearHistory() {
        executor.execute {
            repository.clearHistory()
        }
    }

    override fun getTracksHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        executor.execute {
            consumer.consume(repository.getTracksHistory())
        }
    }

    override fun saveTracksToPrefs(tracks: List<Track>) {
        executor.execute {
            repository.saveTracks(tracks)
        }
    }

}