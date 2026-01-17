package com.example.playlistmaker.db.domain.impl

import android.net.Uri
import com.example.playlistmaker.db.data.storage.ImageStorage
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.db.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository,
    private val imageStorage: ImageStorage,
) : PlaylistsInteractor {

    override fun playlists(): Flow<List<Playlist>> {
        return repository.playlists()
    }

    override suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverIm: Uri,
    ): Int {
        val imageUri = imageStorage.saveImage(coverIm)
        val updated = Playlist(
            name = playlistName,
            description = playlistDescription,
            coverIm = imageUri
        )
        return repository.createPlaylist(updated)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlist = repository.getById(playlistId)
            ?: return
        playlist.coverIm.let {
            imageStorage.deleteImage(it)
        }
        repository.deletePlaylist(playlistId)
    }
}