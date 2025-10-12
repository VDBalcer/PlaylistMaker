package com.example.playlistmaker.search.data.dto.repository

import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.search.domain.model.Track

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {
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