package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult

interface SearchTracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(searchResult: SearchResult)
    }
}