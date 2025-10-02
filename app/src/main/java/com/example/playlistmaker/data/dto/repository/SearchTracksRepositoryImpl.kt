package com.example.playlistmaker.data.dto.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.SearchTracksRepository
import com.example.playlistmaker.domain.models.Track

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) : SearchTracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                it.toDomain()
            }
        } else {
            return emptyList()
        }
    }
}