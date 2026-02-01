package com.example.playlistmaker.db.domain

import android.net.Uri
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    fun playlists(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Int): Flow<Playlist>

    fun getTracksByIds(ids: List<Int>): Flow<List<Track>>

    suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverIm: Uri = Uri.EMPTY,
    ): Int

    suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: String,
        playlistDescription: String,
        coverIm: Uri = Uri.EMPTY,
    )

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)
}