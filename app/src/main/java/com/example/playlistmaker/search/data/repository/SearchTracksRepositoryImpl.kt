package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.search.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<SearchResult> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                emit(
                    SearchResult.Success((response as TracksSearchResponse).results.map {
                        it.toDomain()
                    })
                )
            }

            400 -> {
                emit(SearchResult.ServerError(response.resultCode))
            }

            else -> {
                emit(SearchResult.NetworkError)
            }
        }
    }
}