package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.mock.MockTracks
import com.example.playlistmaker.search.TrackAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val inputEditText = findViewById<TextInputEditText>(R.id.search_input_edit_text)
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val textInputLayout = findViewById<TextInputLayout>(R.id.search_input_layout)
        textInputLayout.setEndIconOnClickListener {
            inputEditText.text?.clear()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.track_recycler_view)
        val trackAdapter = TrackAdapter(MockTracks.trackList)
        recyclerView.adapter = trackAdapter

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT)
        val inputEditText = findViewById<TextInputEditText>(R.id.search_input_edit_text)
        inputEditText.setText(searchText)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}