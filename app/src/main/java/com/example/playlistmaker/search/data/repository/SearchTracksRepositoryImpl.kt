package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.search.domain.model.SearchResult

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {
    override fun searchTracks(expression: String): SearchResult {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            200 -> {
                SearchResult.Success((response as TracksSearchResponse).results.map {
                    it.toDomain()
                })
            }
            400 -> {
                SearchResult.ServerError(response.resultCode)
            }
            else -> {
                SearchResult.NetworkError
            }
        }
    }
}