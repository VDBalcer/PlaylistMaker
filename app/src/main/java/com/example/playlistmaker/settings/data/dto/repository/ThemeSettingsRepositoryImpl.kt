package com.example.playlistmaker.settings.data.dto.repository

import com.example.playlistmaker.settings.data.storage.ThemeStorageClient
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository

class ThemeSettingsRepositoryImpl(
    private val storage: ThemeStorageClient
) : ThemeSettingsRepository {
    override fun isDarkTheme(): Boolean =
        storage.getData()

    override fun setDarkTheme(enabled: Boolean) {
        storage.storeData(enabled)
    }

}