package com.example.playlistmaker.settings.data.dto.repository

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository

class ThemeSettingsRepositoryImpl(private val prefs: SharedPreferences) : ThemeSettingsRepository {
    override fun isDarkTheme(): Boolean =
        prefs.getBoolean(DARK_THEME_KEY, false)

    override fun setDarkTheme(enabled: Boolean) {
        prefs.edit()
            .putBoolean(DARK_THEME_KEY, enabled)
            .apply()
    }

    companion object {
        const val DARK_THEME_KEY = "dark_theme"
    }
}