package com.example.playlistmaker.db.data.converters

import androidx.core.net.toUri
import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor(private val gson: Gson) {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.id,
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            coverIm = playlist.coverIm.toString(),
            idsList = gson.toJson(playlist.idsList),
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        val idsListType = object : TypeToken<List<Int>>() {}.type
        return Playlist(
            id = playlist.playlistId,
            name = playlist.playlistName,
            description = playlist.playlistDescription,
            coverIm = playlist.coverIm.toUri(),
            idsList = gson.fromJson(playlist.idsList, idsListType),
            tracksCount = playlist.tracksCount
        )
    }
}