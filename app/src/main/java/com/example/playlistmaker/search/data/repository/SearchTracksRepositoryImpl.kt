package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.data.converters.TrackDbConvertor
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.search.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

class SearchTracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) :
    SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<SearchResult> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val favoriteIdsSet =
                    database.tracksDao().getTracksIds().first().toSet()
                emit(
                    SearchResult.Success(
                        (response as TracksSearchResponse).results.map { track ->
                            track.toDomain(
                                isFavorite = favoriteIdsSet.contains(track.trackId)
                            )
                        }
                    )
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