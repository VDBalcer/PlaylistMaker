package com.example.playlistmaker.library.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.model.PlaylistsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        playlistsViewModel.getPlaylists()

        binding.placeholderButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_newPlaylistFragment
            )
        }

        playlistAdapter = PlaylistAdapter()
        binding.playlistsRecycler.adapter = playlistAdapter
        binding.playlistsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState){
        when (state) {
            is PlaylistsState.Loading -> showLoading()
            is PlaylistsState.Empty -> showPlaceholder()
            is PlaylistsState.Playlists -> showPlaylists(state.playlists)
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        with(binding){
            placeholder.isVisible = false
            progressBar.isVisible = false
            playlistsRecycler.isVisible = true
            playlistAdapter.playlists = playlists
        }
    }

    private fun showPlaceholder() {
        with(binding){
            playlistsRecycler.isVisible = false
            progressBar.isVisible = false
            placeholder.isVisible = true
        }
    }

    private fun showLoading() {
        with(binding){
            placeholder.isVisible = false
            playlistsRecycler.isVisible = false
            progressBar.isVisible = true
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}