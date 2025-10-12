package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult

interface SearchTracksRepository {
    fun searchTracks(expression: String): SearchResult
}