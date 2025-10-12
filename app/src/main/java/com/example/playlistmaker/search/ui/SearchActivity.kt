package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.DI.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.TrackPlayerActivity
import com.example.playlistmaker.search.ui.model.SearchState
import com.example.playlistmaker.search.domain.model.Track

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getFactory(
            Creator.getTracksInteractor(),
            Creator.getHistoryInteractor(this)
        )
    }

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var tracksHistoryAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) { state ->
            render(state)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        trackAdapter = TrackAdapter(::openTrackPlayer)
        tracksHistoryAdapter = TrackAdapter(::openTrackPlayer)

        binding.trackRecyclerView.adapter = trackAdapter
        binding.tracksHistoryRecyclerView.adapter = tracksHistoryAdapter

        binding.searchInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.searchInputEditText.hasFocus() && s.toString().isEmpty()) {
                    viewModel.showHistory()
                } else {
                    viewModel.searchDebounce(s.toString())
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
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchInputEditText.windowToken, 0)

            binding.trackRecyclerView.visibility = View.GONE
            viewModel.showHistory()
        }

        binding.placeholderButton.setOnClickListener {
            viewModel.searchDebounce(binding.searchInputEditText.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.clickedTracksHistory.visibility = View.GONE
        }
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
            startActivity(Intent(this, TrackPlayerActivity::class.java).apply {
                putExtra(TrackPlayerActivity.TRACK, track)
            })
        }
    }

}