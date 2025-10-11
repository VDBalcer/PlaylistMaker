package com.example.playlistmaker.sharing.domain.model

data class EmailData(
    val emailList: ArrayList<String>,
    val subject: String,
    val text: String
)
