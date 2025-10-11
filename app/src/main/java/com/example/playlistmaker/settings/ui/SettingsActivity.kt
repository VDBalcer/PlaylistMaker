package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_uri))
            startActivity(shareIntent)
        }

        binding.writeSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.author_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.write_support_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.write_support_text))
            startActivity(shareIntent)
        }

        binding.terms.setOnClickListener {
            val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terns_uri)))
            startActivity(termsIntent)
        }

        binding.themeSwitcher.isChecked = (applicationContext as App).themeInteractor.isDarkTheme()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).themeInteractor.updateTheme(checked)
            (applicationContext as App).switchTheme(checked)
        }
    }
}