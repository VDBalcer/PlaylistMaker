package com.example.playlistmaker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(ItunesApi::class.java)

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}