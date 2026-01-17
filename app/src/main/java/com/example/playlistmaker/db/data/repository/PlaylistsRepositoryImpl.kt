package com.example.playlistmaker.db.data.repository

import com.example.playlistmaker.db.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.db.data.dao.PlaylistDao
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConvertor: PlaylistDbConvertor,
) : PlaylistsRepository {


    override fun playlists(): Flow<List<Playlist>> =
        playlistDao
            .getPlaylists()
            .map { playlistEntities ->
                playlistEntities.map { entity ->
                    playlistDbConvertor.map(entity)
                }
            }

    override suspend fun getById(id: Int): Playlist? {
        val entity = playlistDao.getById(id).firstOrNull() ?: return null
        return playlistDbConvertor.map(entity)
    }

    override suspend fun createPlaylist(newPlaylist: Playlist): Int {
        require(newPlaylist.id == null) {
            "Creating playlist with non-null id is forbidden"
        }
        val entity = PlaylistEntity(
            playlistId = 0,
            playlistName = newPlaylist.name,
            playlistDescription = newPlaylist.description,
            coverIm = newPlaylist.coverIm.toString(),
            idsList = "[]",
            tracksCount = 0
        )

        return playlistDao.insertNewPlaylist(entity).toInt()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deleteById(playlistId)
    }

}