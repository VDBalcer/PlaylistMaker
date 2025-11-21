package com.example.playlistmaker.library.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentLibraryFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private var _binding: FragmentLibraryFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}