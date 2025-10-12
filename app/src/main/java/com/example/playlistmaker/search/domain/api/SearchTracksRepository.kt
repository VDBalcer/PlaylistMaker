package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchTracksRepository {
    fun searchTracks(expression: String): List<Track>
}