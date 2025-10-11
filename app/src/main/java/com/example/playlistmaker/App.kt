package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.ThemeInteractor

class App : Application() {
    lateinit var themeInteractor: ThemeInteractor
        private set

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(APP_THEME_PREFERENCES, MODE_PRIVATE)
        themeInteractor = Creator.getThemeInteractor(sharedPrefs)

        // применяем тему при старте
        switchTheme(themeInteractor.isDarkTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        const val APP_THEME_PREFERENCES = "app_theme_preferences"
    }
}