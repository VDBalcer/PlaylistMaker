package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository

class ThemeInteractorImpl(private val repository: ThemeSettingsRepository) : ThemeInteractor {
    override fun isDarkTheme(): Boolean = repository.isDarkTheme()
    override fun updateTheme(enabled: Boolean) = repository.setDarkTheme(enabled)
}