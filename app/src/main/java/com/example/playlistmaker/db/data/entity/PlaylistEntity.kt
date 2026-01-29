package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int?,
    val playlistName: String,
    val playlistDescription: String,
    val coverIm: String,
    val idsList: String,
    val tracksCount: Int,
    val tracksLength:Int
)
