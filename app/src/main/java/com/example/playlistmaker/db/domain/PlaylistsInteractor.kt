package com.example.playlistmaker.db.domain

import android.net.Uri
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    fun playlists(): Flow<List<Playlist>>

    suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverIm: Uri = Uri.EMPTY,
    ): Int

    suspend fun deletePlaylist(playlistId: Int)
}