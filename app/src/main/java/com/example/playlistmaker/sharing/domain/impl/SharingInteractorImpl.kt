package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.ResourceProvider
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private val resourceProvider: ResourceProvider,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(resourceProvider.getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(resourceProvider.getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(resourceProvider.getSupportEmailData())
    }
}