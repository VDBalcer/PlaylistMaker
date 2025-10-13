package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor


class SettingViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeInteractor
) : ViewModel() {
    private val themeLiveData = MutableLiveData<ThemeSettings>()
    fun observeTheme(): LiveData<ThemeSettings> = themeLiveData

    init {
        themeLiveData.value = ThemeSettings(themeInteractor.isDarkTheme())
    }

    fun onShareAppClicked() {
        sharingInteractor.shareApp()
    }

    fun onTermsClicked() {
        sharingInteractor.openTerms()
    }

    fun onSupportClicked() {
        sharingInteractor.openSupport()
    }

    fun onThemeSwitched(enabled: Boolean) {
        themeInteractor.updateTheme(enabled)
        themeLiveData.postValue(ThemeSettings(isDarkTheme = enabled))
    }

    companion object {
        fun getFactory(
            sharingInteractor: SharingInteractor,
            themeInteractor: ThemeInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingViewModel(
                    sharingInteractor = sharingInteractor,
                    themeInteractor = themeInteractor
                )
            }
        }
    }
}