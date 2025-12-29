package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.search.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchTracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) :
    SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<SearchResult> {
        val searchFlow: Flow<Response> = flow {
            val response = networkClient.doRequest(TracksSearchRequest(expression))
            emit(response)
        }

        val favoriteIdsFlow: Flow<Set<Int>> =
            database.tracksDao()
                .getTracksIds()
                .map { it.toSet() }

        return combine(
            searchFlow,
            favoriteIdsFlow
        ) { searchResult, favoriteIdsSet ->
            when (searchResult.resultCode) {
                200 -> {
                    SearchResult.Success(
                        (searchResult as TracksSearchResponse).results.map { track ->
                            track.toDomain(
                                isFavorite = favoriteIdsSet.contains(track.trackId)
                            )
                        }
                    )
                }

                400 -> SearchResult.ServerError(searchResult.resultCode)
                else -> SearchResult.NetworkError
            }
        }
    }
}