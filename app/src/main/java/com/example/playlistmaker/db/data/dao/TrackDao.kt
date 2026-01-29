package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackDao {

    @Query("SELECT * FROM tracks_table WHERE isFavorite = true")
    fun getFavoritesTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM tracks_table WHERE isFavorite = true")
    fun getFavoritesTracksIds(): Flow<List<Int>>

    @Query("SELECT * FROM tracks_table WHERE trackId IN (:ids)")
    fun getTracksByIds(ids: List<Int>): Flow<List<TrackEntity>>

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNewTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("DELETE FROM tracks_table WHERE trackId = :id")
    fun deleteTrackById(id: Int)
}