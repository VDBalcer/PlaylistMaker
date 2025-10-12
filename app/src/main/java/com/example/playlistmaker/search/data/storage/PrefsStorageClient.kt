package com.example.playlistmaker.search.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.settings.data.storage.StorageClient
import com.google.gson.Gson

import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val context: Context,
    private val type: Type
) : StorageClient<T> {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(TRACKS_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun storeData(data: T) {
        prefs.edit().putString(TRACKS_KEY, gson.toJson(data, type)).apply()
    }

    override fun getData(): T? {
        val dataJson = prefs.getString(TRACKS_KEY, null)
        return if (dataJson == null) {
            null
        } else {
            gson.fromJson(dataJson, type)
        }
    }
    companion object{
        const val TRACKS_KEY = "searched_tracks"
    }
}