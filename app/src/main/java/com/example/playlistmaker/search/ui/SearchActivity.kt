package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.TrackPlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.data.dto.repository.SearchHistoryRepositoryImpl.Companion.TRACKS_KEY
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchTracksInteractor
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    var searchText: String? = null

    private var searchInteractor = Creator.getTracksInteractor()

    private var tracks = emptyList<Track>()
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var tracksHistory: List<Track>
    private lateinit var tracksHistoryAdapter: TrackAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private var isTrackClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Подготовка данных для отображения истории треков
        val historyPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistoryInteractor = Creator.getHistoryInteractor(historyPreferences)

        val listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == TRACKS_KEY) {
                searchHistoryInteractor.getTracksHistory(object :
                    SearchHistoryInteractor.HistoryConsumer {
                    override fun consume(historyTracks: List<Track>) {
                        runOnUiThread {
                            tracksHistory = historyTracks
                            tracksHistoryAdapter.tracks = tracksHistory
                            tracksHistoryAdapter.notifyDataSetChanged()
                        }
                    }
                })
            }
        }
        historyPreferences.registerOnSharedPreferenceChangeListener(listener)

        tracksHistoryAdapter = TrackAdapter { clickedTrack ->
            searchHistoryInteractor.addTrackToHistory(clickedTrack)
            searchHistoryInteractor.getTracksHistory(object :
                SearchHistoryInteractor.HistoryConsumer {
                override fun consume(historyTracks: List<Track>) {
                    runOnUiThread {
                        tracksHistory = historyTracks
                        tracksHistoryAdapter.tracks = tracksHistory
                        tracksHistoryAdapter.notifyDataSetChanged()
                    }
                }
            })

            if (clickDebounce()) {
                val intent = Intent(this, TrackPlayerActivity::class.java)
                intent.putExtra(TrackPlayerActivity.TRACK, clickedTrack)
                startActivity(intent)
            }
        }
        searchHistoryInteractor.getTracksHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(historyTracks: List<Track>) {
                runOnUiThread {
                    tracksHistory = historyTracks
                    tracksHistoryAdapter.tracks = tracksHistory
                    tracksHistoryAdapter.notifyDataSetChanged()
                }
            }
        })
        val historyRecyclerView = findViewById<RecyclerView>(R.id.tracks_history_recycler_view)
        historyRecyclerView.adapter = tracksHistoryAdapter


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()

                if (binding.searchInputEditText.hasFocus()
                    && searchText?.isEmpty() == true
                    && tracksHistory.isNotEmpty()
                ) {
                    tracks = emptyList()
                    trackAdapter.tracks = tracks
                    trackAdapter.notifyDataSetChanged()

                    binding.apply {
                        clickedTracksHistory.visibility = View.VISIBLE
                        trackRecyclerView.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }
                    searchRunnable?.let { handler.removeCallbacks(it) }
                } else {
                    searchDebounce(searchText!!)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchInputEditText.addTextChangedListener(simpleTextWatcher)

        // Логика отображения истории
        binding.searchInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && binding.searchInputEditText.text.isNullOrEmpty()
                && tracksHistory.isNotEmpty()
            ) {
                binding.clickedTracksHistory.visibility = View.VISIBLE
            } else binding.clickedTracksHistory.visibility = View.GONE
        }

        val textInputLayout = findViewById<TextInputLayout>(R.id.search_input_layout)
        trackAdapter = TrackAdapter { clickedTrack ->
            searchHistoryInteractor.addTrackToHistory(clickedTrack)
            searchHistoryInteractor.getTracksHistory(object :
                SearchHistoryInteractor.HistoryConsumer {
                override fun consume(historyTracks: List<Track>) {
                    runOnUiThread {
                        tracksHistory = historyTracks
                        tracksHistoryAdapter.tracks = tracksHistory
                        tracksHistoryAdapter.notifyDataSetChanged()
                    }
                }
            })

            if (clickDebounce()) {
                val intent = Intent(this, TrackPlayerActivity::class.java)
                intent.putExtra("track", clickedTrack)
                startActivity(intent)
            }
        }

        // Обработчик крестика в поиске
        textInputLayout.setEndIconOnClickListener {
            binding.searchInputEditText.text?.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchInputEditText.windowToken, 0)

            tracks = emptyList()
            trackAdapter.tracks = tracks
            trackAdapter.notifyDataSetChanged()
            showMessage(null, null, null)
        }

        trackAdapter.tracks = tracks
        binding.trackRecyclerView.adapter = trackAdapter


        binding.placeholderButton.setOnClickListener {
            searchDebounce(binding.searchInputEditText.text.toString())
        }

        // Обработка нажатия на Очистить историю
        val cleanHistoryButton = findViewById<MaterialButton>(R.id.clear_history_button)
        cleanHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            binding.clickedTracksHistory.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT) ?: ""
        binding.searchInputEditText.setText(searchText)

        searchHistoryInteractor.getTracksHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(historyTracks: List<Track>) {
                runOnUiThread {
                    tracksHistory = historyTracks
                    tracksHistoryAdapter.tracks = tracksHistory
                    tracksHistoryAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        searchRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun searchDebounce(text: String) {
        searchRunnable?.let { handler.removeCallbacks(it) }
        searchRunnable = Runnable {
            searchInteractor.searchTracks(text, object : SearchTracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE

                        tracks = if (foundTracks.isNotEmpty()) {
                            showMessage(null, null, null)
                            foundTracks
                        } else {
                            showMessage(R.drawable.im_not_found, getString(R.string.not_found))
                            emptyList()
                        }

                        trackAdapter.tracks = tracks
                        binding.trackRecyclerView.visibility =
                            if (tracks.isNotEmpty()) View.VISIBLE else View.GONE
                        trackAdapter.notifyDataSetChanged()
                    }
                }
            })
        }

        binding.apply {
            trackRecyclerView.visibility = View.GONE
            placeholder.visibility = View.GONE
            clickedTracksHistory.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }

        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showMessage(
        image: Int? = null,
        title: String? = null,
        description: String? = null,
        buttonVisibility: Boolean = false
    ) {

        binding.apply {
            placeholder.visibility =
                if (image == null || title.isNullOrEmpty() || description.isNullOrEmpty())
                    View.VISIBLE
                else
                    View.GONE

            placeholderIm.apply {
                visibility = if (image != null) View.VISIBLE else View.GONE
                image?.let { setImageResource(it) }
            }

            placeholderTitle.apply {
                visibility = if (!title.isNullOrEmpty()) View.VISIBLE else View.GONE
                text = title.orEmpty()
            }

            placeholderDescription.apply {
                visibility = if (!description.isNullOrEmpty()) View.VISIBLE else View.GONE
                text = description.orEmpty()
            }

            placeholderButton.visibility = if (buttonVisibility) View.VISIBLE else View.GONE
        }

    }

    private fun clickDebounce(): Boolean {
        val current = isTrackClickAllowed
        if (isTrackClickAllowed) {
            isTrackClickAllowed = false
            handler.postDelayed({ isTrackClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val HISTORY_PREFERENCES = "search_history_preferences"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}