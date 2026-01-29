package com.example.playlistmaker.library.ui.playlist_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {
    private val viewModel: PlaylistScreenViewModel by viewModel()

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: Playlist

    private var bottomsheetState: Int = BottomSheetBehavior.STATE_HIDDEN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = requireArguments().getParcelable(ARGS_PLAYLIST)!!

        binding.apply {
            playlistTitle.text = playlist.name
            playlistDescription.text = playlist.description
            playlistInfo.text = getPlaylistInfo()
        }
        Glide.with(this).load(playlist.coverIm).placeholder(R.drawable.track_placeholder)
            .fitCenter().into(binding.playlistCoverIm)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.optionsBottomSheet).apply {
            state = bottomsheetState
            if (bottomsheetState == BottomSheetBehavior.STATE_HIDDEN) binding.overlay.isVisible =
                false
            else binding.overlay.isVisible = true
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
//                        viewModel.loadPlaylists()
                    }
                }
                bottomsheetState = newState
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.icMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun getPlaylistInfo(): String {
        val tracksCont = requireContext().resources.getQuantityString(
            R.plurals.track_count, playlist.tracksCount, playlist.tracksCount
        )
        val tracksLength = requireContext().resources.getQuantityString(
            R.plurals.minutes_count, playlist.tracksLength / 60, playlist.tracksLength / 60
        )
        return getString(R.string.playlist_descriptor, tracksLength, tracksCont)
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }
}