package com.example.playlistmaker.settings.data.storage

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}