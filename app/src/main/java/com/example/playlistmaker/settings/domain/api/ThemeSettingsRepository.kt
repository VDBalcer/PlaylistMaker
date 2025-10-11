package com.example.playlistmaker.settings.domain.api

interface ThemeSettingsRepository {
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}