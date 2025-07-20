package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.network.ItunesApi
import com.example.playlistmaker.network.SearchResponse
import com.example.playlistmaker.search.TrackAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    var searchText: String? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ItunesApi::class.java)
    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()
    private lateinit var placeholder : LinearLayout

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
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            showMessage(null, null, null)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.track_recycler_view)
        trackAdapter.tracks = tracks
        recyclerView.adapter = trackAdapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(inputEditText.text.toString(), this)
            }
            false
        }

        val placeholderButton = findViewById<Button>(R.id.placeholder_button)
        placeholderButton.setOnClickListener {
            search(inputEditText.text.toString(), this)
        }
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

    private fun search(text: String, context: Context) {
        iTunesService.search(text).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                tracks.clear()
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        showMessage(null, null, null)
                        val newTracks = response.body()?.results
                            ?.map { it.toDomain() }
                            ?: emptyList()
                        tracks.addAll(newTracks)
                    } else {
                        showMessage(R.drawable.im_not_found, getString(R.string.not_found))
                    }
                } else {
                    showMessage(
                        R.drawable.im_no_internet,
                        getString(R.string.internet_error_title),
                        getString(R.string.internet_error_description),
                        true
                    )
                }
                trackAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                tracks.clear()
                showMessage(
                    R.drawable.im_no_internet,
                    getString(R.string.internet_error_title),
                    getString(R.string.internet_error_description),
                    true
                )
                trackAdapter.notifyDataSetChanged()
            }
        })
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
        placeholder = findViewById<LinearLayout>(R.id.placeholder)


        if ((image != null) or title.isNullOrEmpty() or description.isNullOrEmpty()) {
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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}