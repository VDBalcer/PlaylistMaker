package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.PlaylistEntity;
import com.example.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists_table WHERE playlistId = :id")
    fun getById(id: Int): Flow<PlaylistEntity>

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPlaylist(playlistEntity: PlaylistEntity): Long

    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    fun deleteById(playlistId: Int)
}
