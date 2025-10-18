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

    single<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named("searched_tracks")))
    }

    single<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(get(named("theme_settings")))
    }
}