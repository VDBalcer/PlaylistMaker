package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel : SettingViewModel by viewModel()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            shareApp.setOnClickListener { viewModel.onShareAppClicked() }
            writeSupport.setOnClickListener { viewModel.onSupportClicked() }
            terms.setOnClickListener { viewModel.onTermsClicked() }

            themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.onThemeSwitched(checked)
            }
        }

        viewModel.observeTheme().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it.isDarkTheme
            AppCompatDelegate.setDefaultNightMode(
                if (it.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}