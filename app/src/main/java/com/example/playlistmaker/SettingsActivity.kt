package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.App.Companion.APP_THEME_PREFERENCES
import com.example.playlistmaker.App.Companion.DARK_THEME_KEY
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val shareButton = findViewById<MaterialTextView>(R.id.share_app)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_uri))
            startActivity(shareIntent)
        }

        val supportButton = findViewById<MaterialTextView>(R.id.write_support)
        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.author_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_support_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.write_support_text))
            startActivity(shareIntent)
        }

        val termsButton = findViewById<MaterialTextView>(R.id.terms)
        termsButton.setOnClickListener {
            val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terns_uri)))
            startActivity(termsIntent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            val sharedPrefs = getSharedPreferences(APP_THEME_PREFERENCES, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(DARK_THEME_KEY, checked)
                .apply()
            (applicationContext as App).switchTheme(checked)
        }
    }
}