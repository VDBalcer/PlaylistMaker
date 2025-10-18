package com.example.playlistmaker.DI

import com.example.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.settings.data.repository.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named(SEARCHED_TRACKS_DATA_KEY)))
    }

    factory<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(get(named(THEME_SETTINGS_DATA_KEY)))
    }
}