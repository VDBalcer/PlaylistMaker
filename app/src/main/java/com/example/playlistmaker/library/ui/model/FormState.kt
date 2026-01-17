package com.example.playlistmaker.library.ui.model

import android.net.Uri

sealed class FormState(
    val im: Uri,
    val name: String,
    val description: String,
    val createButtonEnabled: Boolean,
) {
    object Default :
        FormState(Uri.EMPTY, "", "", false)

    class Invalid(im: Uri, name: String, description: String) :
        FormState(im, name, description, false)

    class Valid(im: Uri, name: String, description: String) :
        FormState(im, name, description, true)
}