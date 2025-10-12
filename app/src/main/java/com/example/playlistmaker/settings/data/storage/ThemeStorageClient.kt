package com.example.playlistmaker.settings.data.storage

import android.content.Context
import android.content.SharedPreferences

class ThemeStorageClient(
    private val context: Context,
    private val dataKey: String
) : StorageClient<Boolean> {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(dataKey, Context.MODE_PRIVATE)

    override fun storeData(data: Boolean) {
        prefs.edit().putBoolean(dataKey, data).apply()
    }

    override fun getData(): Boolean {
        return prefs.getBoolean(dataKey, false)
    }
}