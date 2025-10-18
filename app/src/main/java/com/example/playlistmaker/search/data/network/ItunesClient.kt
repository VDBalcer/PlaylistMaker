package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class ItunesClient(private val iTunesService: ItunesApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is TracksSearchRequest) {
                val resp = iTunesService.search(dto.expression).execute()

                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            Response().apply { resultCode = -1 }
        }
    }
}