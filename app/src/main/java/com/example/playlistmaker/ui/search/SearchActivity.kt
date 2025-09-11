package com.example.playlistmaker.ui.search

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
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.player.TrackPlayerActivity
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.dto.repository.SearchHistoryRepositoryImpl.Companion.TRACKS_KEY
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchTracksInteractor
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    var searchText: String? = null

    private var searchInteractor = Creator.getTracksInteractor()

    private var tracks = emptyList<Track>()
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var tracksHistory: List<Track>
    private lateinit var tracksHistoryAdapter: TrackAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var placeholder: LinearLayout
    private lateinit var historyLayout: LinearLayout
    private lateinit var progressBar: ProgressBar

    private var isTrackClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        tracksRecyclerView = findViewById(R.id.track_recycler_view)
        placeholder = findViewById(R.id.placeholder)
        historyLayout = findViewById(R.id.clicked_tracks_history)
        progressBar = findViewById(R.id.progressBar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Подготовка данных для отображения истории треков
        val historyPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistoryInteractor = Creator.getHistoryInteractor(historyPreferences)

        val listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == TRACKS_KEY) {
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
        }
        historyPreferences.registerOnSharedPreferenceChangeListener(listener)

        tracksHistoryAdapter = TrackAdapter { clickedTrack ->
            searchHistoryInteractor.addTrackToHistory(clickedTrack)
            searchHistoryInteractor.getTracksHistory(object : SearchHistoryInteractor.HistoryConsumer {
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

        val inputEditText = findViewById<TextInputEditText>(R.id.search_input_edit_text)
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()

                if (inputEditText.hasFocus()
                    && searchText?.isEmpty() == true
                    && tracksHistory.isNotEmpty()
                ) {
                    tracks = emptyList()
                    trackAdapter.tracks = tracks
                    trackAdapter.notifyDataSetChanged()

                    historyLayout.visibility = View.VISIBLE
                    tracksRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    searchRunnable?.let { handler.removeCallbacks(it) }
                } else {
                    searchDebounce(searchText!!)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        // Логика отображения истории
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && inputEditText.text.isNullOrEmpty()
                && tracksHistory.isNotEmpty()
            ) {
                historyLayout.visibility = View.VISIBLE
            } else historyLayout.visibility = View.GONE
        }

        val textInputLayout = findViewById<TextInputLayout>(R.id.search_input_layout)
        trackAdapter = TrackAdapter { clickedTrack ->
            searchHistoryInteractor.addTrackToHistory(clickedTrack)
            searchHistoryInteractor.getTracksHistory(object : SearchHistoryInteractor.HistoryConsumer {
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
            inputEditText.text?.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)

            tracks = emptyList()
            trackAdapter.tracks = tracks
            trackAdapter.notifyDataSetChanged()
            showMessage(null, null, null)
        }

        trackAdapter.tracks = tracks
        tracksRecyclerView.adapter = trackAdapter

        val placeholderButton = findViewById<Button>(R.id.placeholder_button)
        placeholderButton.setOnClickListener {
            searchDebounce(inputEditText.text.toString())
        }

        // Обработка нажатия на Очистить историю
        val cleanHistoryButton = findViewById<MaterialButton>(R.id.clear_history_button)
        cleanHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            historyLayout.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT) ?: ""
        findViewById<TextInputEditText>(R.id.search_input_edit_text).setText(searchText)

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
                        progressBar.visibility = View.GONE

                        tracks = if (foundTracks.isNotEmpty()) {
                            showMessage(null, null, null)
                            foundTracks
                        } else {
                            showMessage(R.drawable.im_not_found, getString(R.string.not_found))
                            emptyList()
                        }

                        trackAdapter.tracks = tracks
                        tracksRecyclerView.visibility =
                            if (tracks.isNotEmpty()) View.VISIBLE else View.GONE
                        trackAdapter.notifyDataSetChanged()
                    }
                }
            })
        }

        tracksRecyclerView.visibility = View.GONE
        placeholder.visibility = View.GONE
        historyLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showMessage(
        image: Int? = null,
        title: String? = null,
        description: String? = null,
        buttonVisibility: Boolean = false
    ) {
        val placeholderImage = findViewById<ImageView>(R.id.placeholder_im)
        val placeholderTitle = findViewById<TextView>(R.id.placeholder_title)
        val placeholderDescription = findViewById<TextView>(R.id.placeholder_description)
        val placeholderButton = findViewById<Button>(R.id.placeholder_button)

        if ((image != null) || title.isNullOrEmpty() || description.isNullOrEmpty()) {
            placeholder.visibility = View.VISIBLE
        } else {
            placeholder.visibility = View.GONE
        }
        if (image != null) {
            placeholderImage.setImageResource(image)
            placeholderImage.visibility = View.VISIBLE
        } else {
            placeholderImage.visibility = View.GONE
        }

        if (!title.isNullOrEmpty()) {
            placeholderTitle.text = title
            placeholderTitle.visibility = View.VISIBLE
        } else {
            placeholderTitle.visibility = View.GONE
        }
        if (!description.isNullOrEmpty()) {
            placeholderDescription.text = description
            placeholderDescription.visibility = View.VISIBLE
        } else {
            placeholderDescription.visibility = View.GONE
        }

        if (buttonVisibility) {
            placeholderButton.visibility = View.VISIBLE
        } else {
            placeholderButton.visibility = View.GONE
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