package com.example.playlistmaker.db.domain

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun playlists(): Flow<List<Playlist>>

    suspend fun getById(id: Int): Playlist?

    suspend fun createPlaylist(newPlaylist: Playlist):Int

    suspend fun updatePlaylist(newPlaylist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrack(track:Track)
}