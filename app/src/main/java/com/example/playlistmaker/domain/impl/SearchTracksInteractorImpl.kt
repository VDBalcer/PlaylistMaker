package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchTracksInteractor
import com.example.playlistmaker.domain.api.SearchTracksRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: SearchTracksRepository) : SearchTracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}