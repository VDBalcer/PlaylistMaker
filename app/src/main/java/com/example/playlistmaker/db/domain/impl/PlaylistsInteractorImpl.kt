package com.example.playlistmaker.db.domain.impl

import android.net.Uri
import com.example.playlistmaker.db.data.storage.ImageStorage
import com.example.playlistmaker.db.domain.PlaylistsInteractor
import com.example.playlistmaker.db.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository,
    private val imageStorage: ImageStorage,
) : PlaylistsInteractor {

    override fun playlists(): Flow<List<Playlist>> {
        return repository.playlists()
    }

    override fun getPlaylistById(playlistId: Int): Flow<Playlist> {
        return repository.getById(playlistId)
    }

    override fun getTracksByIds(ids: List<Int>): Flow<List<Track>> {
        return repository.getTracksByIds(ids)
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

    override suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: String,
        playlistDescription: String,
        coverIm: Uri,
    ) {
        val playlist = repository.getById(playlistId).first()
        playlist.name = playlistName
        playlist.description = playlistDescription
        if (coverIm != playlist.coverIm) {
            imageStorage.deleteImage(playlist.coverIm)
            playlist.coverIm = imageStorage.saveImage(coverIm)
        } else {
            playlist.coverIm
        }
        return repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        withContext(Dispatchers.IO) {
            val playlist = repository.getById(playlistId).first()
            val tracks = repository.getTracksByIds(playlist.idsList).first()

            playlist.coverIm.let {
                imageStorage.deleteImage(it)
            }
            repository.deletePlaylist(playlistId)

            val idsInPlaylists = repository.getAllTracksInPlaylists()
            tracks.forEach { track ->
                val isNotUsedInPlaylists = track.trackId !in idsInPlaylists
                val isNotFavorite = !track.isFavorite

                if (isNotUsedInPlaylists && isNotFavorite) {
                    repository.deleteTrackById(track.trackId)
                }
            }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            repository.addTrack(track)
            playlist.idsList += track.trackId
            playlist.tracksCount += 1
            repository.updatePlaylist(playlist)
        }
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlist.idsList -= track.trackId
            playlist.tracksCount -= 1
            repository.updatePlaylist(playlist)
            val idsInPlaylists = repository.getAllTracksInPlaylists()

            val isNotUsedInPlaylists = track.trackId !in idsInPlaylists
            val isNotFavorite = !track.isFavorite

            if (isNotUsedInPlaylists && isNotFavorite) {
                repository.deleteTrackById(track.trackId)
            }
        }
    }
}