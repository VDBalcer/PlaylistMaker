package com.example.playlistmaker.library.ui.playlist_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.library.ui.favorite.FavoriteFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.library.ui.new_playlist.EditPlaylistFragment
import com.example.playlistmaker.library.ui.playlist_screen.model.PlaylistScreenState
import com.example.playlistmaker.player.ui.TrackPlayerFragment
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {
    private val viewModel: PlaylistScreenViewModel by viewModel()

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private var bottomsheetState: Int = BottomSheetBehavior.STATE_HIDDEN

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onTrackLongClick: (Track) -> Unit

    private var isTracksEmpty = true

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
        val playlistId = requireArguments().getInt(ARGS_PLAYLIST_ID)
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_playlistScreenFragment_to_trackPlayerFragment,
                TrackPlayerFragment.createArgs(track)
            )
        }
        onTrackLongClick = { track: Track ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.remove_track_dialog_title))
                .setNegativeButton(getString(R.string.remove_track_dialog_negative_text), null)
                .setPositiveButton(getString(R.string.remove_track_dialog_positive_text)) { _, _ ->
                    viewModel.deleteTrack(track)
                }
                .create()
                .show()
        }
        trackAdapter = TrackAdapter(onTrackClickDebounce, onTrackLongClick)
        binding.tracksRecyclerBottomSheet.adapter = trackAdapter

        viewModel.observePlaylist().observe(viewLifecycleOwner) { state ->
            binding.apply {
                playlistTitle.text = state.playlist.name
                playlistDescription.text = state.playlist.description
                playlistInfo.text = getPlaylistInfo(state)
                Glide.with(this@PlaylistScreenFragment)
                    .load(state.playlist.coverIm)
                    .placeholder(R.drawable.track_placeholder)
                    .fitCenter()
                    .into(playlistCoverIm)
            }

            isTracksEmpty = state.tracks.isEmpty()
            trackAdapter.tracks = state.tracks
            trackAdapter.notifyDataSetChanged()

            binding.itemPlaylistHorizontal.apply {
                Glide.with(this@PlaylistScreenFragment).load(state.playlist.coverIm)
                    .placeholder(R.drawable.track_placeholder)
                    .fitCenter().into(playlistCoverIm)
                playlistTitle.text = state.playlist.name
                playlistDescription.text = requireContext().resources.getQuantityString(
                    R.plurals.track_count, state.playlist.tracksCount, state.playlist.tracksCount
                )
            }

        }
        viewModel.loadPlaylistInfo(playlistId)

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
                    }
                }
                bottomsheetState = newState
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        val emptyListToast = Toast.makeText(
            context,
            getString(R.string.empty_playlist_toast_text),
            Toast.LENGTH_SHORT
        )
        binding.icShare.setOnClickListener {
            if (isTracksEmpty) emptyListToast.show()
            else viewModel.onSharePlaylistClicked(playlistId)
        }
        binding.icMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.sharePlaylist.setOnClickListener {
            if (isTracksEmpty) emptyListToast.show()
            else viewModel.onSharePlaylistClicked(playlistId)
        }
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист?")
            .setNegativeButton(getString(R.string.remove_track_dialog_negative_text), null)
            .setPositiveButton(getString(R.string.remove_track_dialog_positive_text)) { _, _ ->
                viewModel.deletePlaylist(playlistId)
                findNavController().popBackStack()
            }
            .create()
        binding.deletePlaylist.setOnClickListener {
            confirmDialog.show()
        }
        binding.editPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistScreenFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlistId)
            )
        }


    }

    private fun getPlaylistInfo(state: PlaylistScreenState): String {
        val tracksCont = requireContext().resources.getQuantityString(
            R.plurals.track_count, state.playlist.tracksCount, state.playlist.tracksCount
        )
        val tracksLength = requireContext().resources.getQuantityString(
            R.plurals.minutes_count,
            state.totalDurationSeconds / 60,
            state.totalDurationSeconds / 60
        )
        return getString(R.string.playlist_descriptor, tracksLength, tracksCont)
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlistId"
        fun createArgs(playlistId: Int): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }
}