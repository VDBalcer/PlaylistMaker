package com.example.playlistmaker.domain.api

interface ThemeInteractor {
    fun isDarkTheme(): Boolean
    fun updateTheme(enabled: Boolean)
}