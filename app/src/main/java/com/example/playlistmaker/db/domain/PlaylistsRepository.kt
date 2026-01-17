package com.example.playlistmaker.db.domain

import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun playlists(): Flow<List<Playlist>>

    suspend fun getById(id: Int): Playlist?

    suspend fun createPlaylist(newPlaylist: Playlist):Int

    suspend fun deletePlaylist(playlistId: Int)
}