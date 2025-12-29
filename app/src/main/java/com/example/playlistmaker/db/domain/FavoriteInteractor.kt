package com.example.playlistmaker.db.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun favoriteTracks(): Flow<List<Track>>

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)
}