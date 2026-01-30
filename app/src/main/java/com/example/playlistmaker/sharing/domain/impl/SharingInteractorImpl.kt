package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.db.domain.PlaylistsRepository
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.ResourceProvider
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.flow.first

class SharingInteractorImpl(
    private val resourceProvider: ResourceProvider,
    private val externalNavigator: ExternalNavigator,
    private val playlistsRepository: PlaylistsRepository
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

    override suspend fun sharePlaylist(playlistId: Int) {
        val playlist = playlistsRepository.getById(playlistId).first()
        val tracks = playlistsRepository.getTracksByIds(playlist.idsList).first()
        val message = buildString {
            appendLine(playlist.name)
            if (playlist.description.isNotBlank()) {
                appendLine(playlist.description)
            }
            appendLine(resourceProvider.getTrackCountString(tracks.size))

            tracks.forEachIndexed { index, track ->
                appendLine("${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})")
            }
        }
        externalNavigator.shareLink(message)
    }
}