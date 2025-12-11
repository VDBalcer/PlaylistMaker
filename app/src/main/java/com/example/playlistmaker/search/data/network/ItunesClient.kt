package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItunesClient(private val iTunesService: ItunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (dto is TracksSearchRequest) {
                    val resp = iTunesService.search(dto.expression)
                    resp.apply { resultCode = 200 }
                } else {
                    Response().apply { resultCode = 400 }
                }
            } catch (e: Throwable) {
                Response().apply { resultCode = -1 }
            }
        }

    }
}