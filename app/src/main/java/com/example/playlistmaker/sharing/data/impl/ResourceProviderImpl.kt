package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ResourceProvider
import com.example.playlistmaker.sharing.domain.model.EmailData

class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {

    override fun getShareAppLink(): String =
        context.getString(R.string.share_uri)

    override fun getSupportEmailData(): EmailData =
        EmailData(
            emailList = arrayListOf(context.getString(R.string.author_email)),
            subject = context.getString(R.string.write_support_subject),
            text = context.getString(R.string.write_support_text)
        )

    override fun getTermsLink(): String =
        context.getString(R.string.terns_uri)
}