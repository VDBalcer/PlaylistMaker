package com.example.playlistmaker.domain.api

interface ThemeSettingsRepository {
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}