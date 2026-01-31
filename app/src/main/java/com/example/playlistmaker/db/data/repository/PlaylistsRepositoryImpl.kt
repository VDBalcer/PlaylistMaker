package com.example.playlistmaker.db.data.repository

import com.example.playlistmaker.db.data.converters.PlaylistDbConvertor
import com.example.playlistmaker.db.data.converters.TrackDbConvertor
import com.example.playlistmaker.db.data.dao.PlaylistDao
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackDbConvertor: TrackDbConvertor,
    private val trackDao: TrackDao,
    private val gson: Gson,
) : PlaylistsRepository {


    override fun playlists(): Flow<List<Playlist>> =
        playlistDao
            .getPlaylists()
            .map { playlistEntities ->
                playlistEntities.map { entity ->
                    playlistDbConvertor.map(entity)
                }
            }

    override fun getById(id: Int): Flow<Playlist> =
        playlistDao
            .getPlaylistById(id)
            .map { playlistEntity ->
                playlistDbConvertor.map(playlistEntity)
            }

    override fun getTracksByIds(ids: List<Int>): Flow<List<Track>> =
        trackDao
            .getTracksByIds(ids)
            .map { tracksList ->
                tracksList.map { trackEntity ->
                    trackDbConvertor.map(trackEntity)
                }
            }

    override suspend fun createPlaylist(newPlaylist: Playlist): Int {
        require(newPlaylist.id == null) {
            "Creating playlist with non-null id is forbidden"
        }
        val entity = PlaylistEntity(
            playlistId = null,
            playlistName = newPlaylist.name,
            playlistDescription = newPlaylist.description,
            coverIm = newPlaylist.coverIm.toString(),
            idsList = "[]",
            tracksCount = 0
        )

        return playlistDao.insertNewPlaylist(entity).toInt()
    }

    override suspend fun updatePlaylist(newPlaylist: Playlist) {
        val entity = playlistDbConvertor.map(newPlaylist)
        playlistDao.updatePlaylist(entity)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylistById(playlistId)
    }

    override suspend fun addTrack(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        playlistDao.insertNewTrack(trackEntity)
    }

    override suspend fun getAllTracksInPlaylists(): List<Int> {
        val idsListType = object : TypeToken<List<Int>>() {}.type
        return playlistDao.getAllTracksInPlaylists().flatMap { json ->
            val list: List<Int> = gson.fromJson(json, idsListType)
            list
        }
    }

    override suspend fun deleteTrackById(trackId: Int) {
        trackDao.deleteTrackById(trackId)
    }
}