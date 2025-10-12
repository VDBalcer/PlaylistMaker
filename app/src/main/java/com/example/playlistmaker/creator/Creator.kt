package com.example.playlistmaker.creator

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.dto.repository.SearchTracksRepositoryImpl
import com.example.playlistmaker.settings.data.dto.repository.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.search.data.network.ItunesClient
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksRepository
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSettingsRepository
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchTracksInteractorImpl

object Creator {

    fun getTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    private fun getTracksRepository(): SearchTracksRepository {
        return SearchTracksRepositoryImpl(ItunesClient())
    }

    fun getPlayerInteractor(trackUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(trackUrl)
    }

    fun getHistoryInteractor(historyPreferences: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getHistoryRepository(historyPreferences))
    }

    private fun getHistoryRepository(historyPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(preferences = historyPreferences)
    }

    fun getThemeInteractor(themePreferences: SharedPreferences): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(themePreferences))
    }

    private fun getThemeRepository(themePreferences: SharedPreferences): ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(themePreferences)
    }
}