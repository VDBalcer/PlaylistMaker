package com.example.playlistmaker.data.dto.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val preferences: SharedPreferences, private val gson : Gson = Gson()) : SearchHistoryRepository {

    override fun addTrackToHistory(newTrack: Track) {
        val updated = getUpdatedTrackList(newTrack)
        saveTracksToPrefs(updated)
    }

    override fun clearHistory() {
        preferences.edit().remove(TRACKS_KEY).apply()
    }

    override fun getTracksHistory(): List<Track> {
        val json = preferences.getString(TRACKS_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    override fun saveTracksToPrefs(tracks: List<Track>) {
        val updatedJson = gson.toJson(tracks)
        preferences.edit().putString(TRACKS_KEY, updatedJson).apply()
    }

    override fun getUpdatedTrackList(newTrack: Track): MutableList<Track> {
        val tracks = getTracksHistory().toMutableList()
        val existingIndex = tracks.indexOfFirst { it.trackId == newTrack.trackId }
        if (existingIndex != -1) tracks.removeAt(existingIndex)
        tracks.add(0, newTrack)
        if (tracks.size > TRACKS_COUNT) tracks.subList(TRACKS_COUNT, tracks.size).clear()
        return tracks
    }

    companion object {
        const val TRACKS_KEY = "searched_tracks"
        const val TRACKS_COUNT = 10
    }
}