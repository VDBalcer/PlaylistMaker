package com.example.playlistmaker.library.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryFavoriteBinding
import com.example.playlistmaker.player.ui.TrackPlayerFragment
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModel()

    private var _binding: FragmentLibraryFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLibraryFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_trackPlayerFragment,
                TrackPlayerFragment.createArgs(track)
            )
        }
        trackAdapter = TrackAdapter(onTrackClickDebounce)
        binding.trackRecyclerView.adapter = trackAdapter

        viewModel.fillFavourite()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun render(state: FavoriteViewModel.FavoriteState) {
        when (state) {
            is FavoriteViewModel.FavoriteState.Loading -> showLoading()
            is FavoriteViewModel.FavoriteState.Favorite -> showFavorite(state.tracks)
            is FavoriteViewModel.FavoriteState.Empty -> showEmpty()
        }
    }

    private fun showFavorite(tracks: List<Track>) {
        binding.apply {
            progressBar.visibility = View.GONE
            placeholder.visibility = View.GONE
            trackRecyclerView.visibility = View.VISIBLE
            trackAdapter.tracks = tracks
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun showLoading() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            placeholder.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showEmpty() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }


    companion object {
        fun newInstance() = FavoriteFragment()

        const val CLICK_DEBOUNCE_DELAY = 0L
    }
}