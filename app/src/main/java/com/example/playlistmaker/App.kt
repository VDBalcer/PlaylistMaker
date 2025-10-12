package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.DI.Creator
import com.example.playlistmaker.settings.domain.api.ThemeInteractor

class App : Application() {
    lateinit var themeInteractor: ThemeInteractor
        private set

    override fun onCreate() {
        super.onCreate()

        themeInteractor = Creator.getThemeInteractor(this)

        // применяем тему при старте
        switchTheme(themeInteractor.isDarkTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}