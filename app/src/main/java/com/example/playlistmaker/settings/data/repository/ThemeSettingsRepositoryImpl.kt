package com.example.playlistmaker.settings.data.repository

import com.example.playlistmaker.settings.data.storage.StorageClient
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class ThemeSettingsRepositoryImpl(
    private val storage: StorageClient<ThemeSettings>
) : ThemeSettingsRepository {
    override fun isDarkTheme(): Boolean =
        storage.getData()?.isDarkTheme ?: false

    override fun setDarkTheme(enabled: Boolean) {
        storage.storeData(ThemeSettings(enabled))
    }

}