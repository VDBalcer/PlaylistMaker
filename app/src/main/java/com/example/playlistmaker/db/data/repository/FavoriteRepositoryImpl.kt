package com.example.playlistmaker.db.data.repository

import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.data.converters.TrackDbConvertor
import com.example.playlistmaker.db.domain.FavoriteRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {

    override fun favoriteTracks(): Flow<List<Track>> =
        appDatabase.tracksDao()
            .getTracks()
            .map { trackEntities ->
                trackEntities.map { entity ->
                    trackDbConvertor.map(entity)
                }
            }

    override suspend fun addTrack(newTrack: Track) {
        withContext(Dispatchers.IO) {
            val trackEntity = trackDbConvertor.map(newTrack)
            appDatabase.tracksDao().insertNewTrack(trackEntity)
        }
    }

    override suspend fun deleteTrack(track: Track) {
        withContext(Dispatchers.IO) {
            val trackEntity = trackDbConvertor.map(track)
            appDatabase.tracksDao().deleteTrackEntity(trackEntity)
        }
    }
}