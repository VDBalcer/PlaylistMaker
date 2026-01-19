package com.example.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.dao.PlaylistDao
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.db.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun tracksDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}