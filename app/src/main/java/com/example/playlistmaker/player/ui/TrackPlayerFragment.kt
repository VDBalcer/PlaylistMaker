package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackPlayerBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.player.ui.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerFragment : Fragment() {
    private var _binding: TrackPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var track: Track
    private val viewModel: TrackPlayerViewModel by viewModel {
        parametersOf(track)
    }
    private var bottomsheetState: Int = BottomSheetBehavior.STATE_HIDDEN

    private lateinit var playlistAdapter: PlaylistHorizontalAdapter
    private val onPlaylistClick = { playlist: Playlist ->
        viewModel.onPlaylistClick(playlist)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = TrackPlayerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        track = requireArguments().getParcelable(ARGS_TRACK)!!

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
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
                        viewModel.loadPlaylists()
                    }
                }
                bottomsheetState = newState
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.apply {
            trackTitle.text = track.trackName
            artistName.text = track.artistName
            trackTimeContent.text = track.trackTime
            trackAlbumContent.text = track.collectionName
            trackYearContent.text = track.releaseDate.let { raw ->
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    .parse(raw)
                    ?.let { parsed ->
                        SimpleDateFormat("yyyy", Locale.getDefault()).format(parsed)
                    }
            }
            trackGenreContent.text = track.primaryGenreName
            trackCountryContent.text = track.country
        }
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, requireContext())))
            .into(binding.trackArtworkImage)


        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            binding.trackTimer.text = it.progress
            when (it) {
                is PlayerState.Playing ->
                    binding.mainPlayerButton.setImageResource(R.drawable.ic_stop)

                else ->
                    binding.mainPlayerButton.setImageResource(R.drawable.ic_play)
            }
            if (it.isFavorite) {
                binding.addFavoriteButton.setImageResource(R.drawable.ic_heart)
            } else {
                binding.addFavoriteButton.setImageResource(R.drawable.ic_unchoosed_heart)
            }
        }
        binding.mainPlayerButton.setOnClickListener {
            viewModel.playButtonClick()
        }
        binding.addFavoriteButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
        binding.addTrackButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.placeholderButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_trackPlayerFragment_to_newPlaylistFragment
            )
        }
        playlistAdapter = PlaylistHorizontalAdapter(onPlaylistClick)
        binding.playlistsRecyclerBottomSheet.adapter = playlistAdapter
        viewModel.observePlaylists().observe(viewLifecycleOwner) {
            playlistAdapter.playlists = it
            playlistAdapter.notifyDataSetChanged()
        }
        viewModel.observeAddTrackStatus().observe(viewLifecycleOwner) { status ->
            val message: String
            when (status) {
                is AddTrackStatus.Added -> {
                    message = getString(
                        R.string.track_player_add_to_playlist_success_message,
                        status.playlistName
                    )
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is AddTrackStatus.AlreadyExists ->
                    message = getString(
                        R.string.track_player_already_added_to_playlist_message,
                        status.playlistName
                    )
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARTWORK_RADIUS = 8f
        private const val ARGS_TRACK = "track"

        const val TAG = "TrackPlayerFragment"

        fun newInstance(track: Track): Fragment {
            return TrackPlayerFragment().apply {
                arguments = createArgs(track)
            }
        }

        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }
}