package com.example.playlistmaker.sharing.domain.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }


    private fun getShareAppLink(): String =
        context.getString(R.string.share_uri)

    private fun getSupportEmailData(): EmailData =
        EmailData(
            emailList = arrayListOf(context.getString(R.string.author_email)),
            subject = context.getString(R.string.write_support_subject),
            text = context.getString(R.string.write_support_text)
        )

    private fun getTermsLink(): String =
        context.getString(R.string.terns_uri)
}