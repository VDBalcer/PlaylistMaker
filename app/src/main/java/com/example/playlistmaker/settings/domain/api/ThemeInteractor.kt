package com.example.playlistmaker.settings.domain.api

interface ThemeInteractor {
    fun isDarkTheme(): Boolean
    fun updateTheme(enabled: Boolean)
}