package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }

            shareApp.setOnClickListener { viewModel.onShareAppClicked() }
            writeSupport.setOnClickListener { viewModel.onSupportClicked() }
            terms.setOnClickListener { viewModel.onTermsClicked() }

            themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.onThemeSwitched(checked)
            }
        }

        viewModel.observeTheme().observe(this@SettingsActivity) {
            binding.themeSwitcher.isChecked = it.isDarkTheme
            AppCompatDelegate.setDefaultNightMode(
                if (it.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}