package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.TrackPlayerFragment
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.model.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var tracksHistoryAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        trackAdapter = TrackAdapter(::openTrackPlayer)
        tracksHistoryAdapter = TrackAdapter(::openTrackPlayer)

        binding.trackRecyclerView.adapter = trackAdapter
        binding.tracksHistoryRecyclerView.adapter = tracksHistoryAdapter

        binding.searchInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchInputLayout.isEndIconVisible = s.toString().isNotEmpty()
                if (!binding.searchInputEditText.hasFocus()){
                    return
                } else{
                    if (s.toString().isEmpty()) {
                        viewModel.showHistory()
                    } else {
                        viewModel.searchDebounce(s.toString())
                    }
                }
            }
        })

        binding.searchInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchInputEditText.text.isNullOrEmpty()) {
                viewModel.showHistory()
            }
        }

        binding.searchInputLayout.setEndIconOnClickListener {
            binding.searchInputEditText.text?.clear()
            val imm = requireContext().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            binding.trackRecyclerView.visibility = View.GONE
            binding.searchInputLayout.isEndIconVisible = false
            viewModel.showHistory()
        }
        binding.searchInputLayout.isEndIconVisible = binding.searchInputEditText.text.toString().isNotEmpty()

        binding.placeholderButton.setOnClickListener {
            viewModel.searchDebounce(binding.searchInputEditText.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.clickedTracksHistory.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Search -> showSearchResult(state.tracks)
            is SearchState.History -> showHistory(state.tracks)
            is SearchState.Error -> showError()
            is SearchState.Empty -> showEmpty()
        }
    }

    private fun showError() {
        binding.apply {
            progressBar.visibility = View.GONE
            trackRecyclerView.visibility = View.GONE
            clickedTracksHistory.visibility = View.GONE

            placeholder.visibility = View.VISIBLE
            placeholderIm.apply {
                visibility = View.VISIBLE
                setImageResource(R.drawable.im_no_internet)
            }
            placeholderTitle.apply {
                visibility = View.VISIBLE
                text = getString(R.string.internet_error_title)
            }
            placeholderDescription.apply {
                visibility = View.VISIBLE
                text = getString(R.string.internet_error_description)
            }
            placeholderButton.apply {
                visibility = View.VISIBLE
                text = getString(R.string.refresh)
            }
        }
    }

    private fun showEmpty() {
        binding.apply {
            progressBar.visibility = View.GONE
            trackRecyclerView.visibility = View.GONE
            clickedTracksHistory.visibility = View.GONE

            placeholder.visibility = View.VISIBLE
            placeholderIm.apply {
                visibility = View.VISIBLE
                setImageResource(R.drawable.im_not_found)
            }
            placeholderTitle.apply {
                visibility = View.VISIBLE
                text = getString(R.string.not_found)
            }
            placeholderDescription.visibility = View.GONE
            placeholderButton.visibility = View.GONE
        }
    }

    private fun showHistory(tracks: List<Track>) {
        binding.apply {
            progressBar.visibility = View.GONE
            placeholder.visibility = View.GONE
            trackRecyclerView.visibility = View.GONE
            clickedTracksHistory.visibility = if (tracks.isNotEmpty()) View.VISIBLE else View.GONE
            tracksHistoryAdapter.tracks = tracks
            tracksHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun showSearchResult(tracks: List<Track>) {
        binding.apply {
            progressBar.visibility = View.GONE
            placeholder.visibility = View.GONE
            trackRecyclerView.visibility = View.VISIBLE
            clickedTracksHistory.visibility = View.GONE
            trackAdapter.tracks = tracks
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun showLoading() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            clickedTracksHistory.visibility = View.GONE
            placeholder.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun openTrackPlayer(track: Track) {
        if (viewModel.onTrackClicked(track)) {
            findNavController().navigate(
                R.id.action_searchFragment_to_trackPlayerFragment,
                TrackPlayerFragment.createArgs(track)
            )
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}