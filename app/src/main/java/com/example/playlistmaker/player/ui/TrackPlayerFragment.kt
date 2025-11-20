package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerFragment : Fragment() {
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


    private lateinit var binding: TrackPlayerBinding
    private lateinit var track: Track
    private val viewModel: TrackPlayerViewModel by viewModel {
        parametersOf(track.previewUrl)
        //TODO Оставить что-то одно
//        parametersOf(requireArguments().getParcelable<Track>(ARGS_TRACK)!!.previewUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TrackPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = requireArguments().getParcelable(ARGS_TRACK)!!

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

        viewModel.observeProgressTime().observe(viewLifecycleOwner) {
            binding.trackTimer.text = it
        }
        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            when (it) {
                TrackPlayerViewModel.STATE_PLAYING ->
                    binding.mainPlayerButton.setImageResource(R.drawable.ic_stop)

                else ->
                    binding.mainPlayerButton.setImageResource(R.drawable.ic_play)
            }
        }
        binding.mainPlayerButton.setOnClickListener {
            viewModel.playButtonClick()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releasePlayer()
    }
}