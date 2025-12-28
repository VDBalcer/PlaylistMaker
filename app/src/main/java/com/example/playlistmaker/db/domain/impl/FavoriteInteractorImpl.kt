package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.FavoriteInteractor
import com.example.playlistmaker.db.domain.FavoriteRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
) : FavoriteInteractor {

    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.favoriteTracks()
    }

    override suspend fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }


    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }
}