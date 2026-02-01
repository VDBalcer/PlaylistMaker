package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.FavoriteInteractor
import com.example.playlistmaker.db.domain.FavoriteRepository
import com.example.playlistmaker.db.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
    private val playlistsRepository: PlaylistsRepository,
) : FavoriteInteractor {

    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.favoriteTracks()
    }

    override suspend fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }


    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            val idsInPlaylists = playlistsRepository.getAllTracksInPlaylists()

            val isNotUsedInPlaylists = track.trackId !in idsInPlaylists
            val isNotFavorite = !track.isFavorite

            if (isNotUsedInPlaylists && isNotFavorite) {
                favoriteRepository.deleteTrack(track)
            } else favoriteRepository.addTrack(track)
        }
    }
}