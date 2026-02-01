package com.example.playlistmaker.db.domain

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun playlists(): Flow<List<Playlist>>

    fun getById(id: Int): Flow<Playlist>

    fun getTracksByIds(ids: List<Int>): Flow<List<Track>>

    suspend fun createPlaylist(newPlaylist: Playlist):Int

    suspend fun updatePlaylist(newPlaylist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrack(track:Track)

    suspend fun getAllTracksInPlaylists(): List<Int>

    suspend fun deleteTrackById(trackId: Int)
}