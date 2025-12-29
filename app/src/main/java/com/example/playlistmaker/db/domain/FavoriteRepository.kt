package com.example.playlistmaker.db.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun favoriteTracks(): Flow<List<Track>>

    suspend fun addTrack(newTrack:Track)

    suspend fun deleteTrack(track: Track)
}