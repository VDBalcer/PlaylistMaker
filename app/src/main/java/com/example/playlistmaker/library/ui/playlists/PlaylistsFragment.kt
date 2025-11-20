package com.example.playlistmaker.library.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val playlistsViewModel : PlaylistsViewModel by viewModel()

    private lateinit var binding: FragmentLibraryPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }
}