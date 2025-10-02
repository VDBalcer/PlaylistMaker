package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.dto.repository.SearchTracksRepositoryImpl
import com.example.playlistmaker.data.dto.repository.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.data.network.ItunesClient
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.SearchTracksInteractor
import com.example.playlistmaker.domain.api.SearchTracksRepository
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ThemeSettingsRepository
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SearchTracksInteractorImpl

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