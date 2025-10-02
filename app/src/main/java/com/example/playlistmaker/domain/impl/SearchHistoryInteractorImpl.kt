package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
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
            repository.saveTracksToPrefs(tracks)
        }
    }

}