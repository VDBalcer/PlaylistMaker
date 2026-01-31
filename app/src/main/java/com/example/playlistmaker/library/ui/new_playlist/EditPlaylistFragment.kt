package com.example.playlistmaker.library.ui.new_playlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : NewPlaylistFragment() {
    private val viewModel: EditPlaylistViewModel by viewModel {
        parametersOf(requireArguments().getInt(ARGS_PLAYLIST))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createPlaylistButton.setText(getString(R.string.edit_playlist_button_text))

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.track_placeholder)
                        .fitCenter()
                        .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, requireContext())))
                        .into(binding.addPhotoIm)
                    viewModel.updateState(im = uri)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.nothing_choosen_toast_text), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        binding.addPhotoIm.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.playlistNameInputEditText.doAfterTextChanged {
            viewModel.updateState(name = it.toString())
        }
        binding.playlistDescriptionInputEditText.doAfterTextChanged {
            viewModel.updateState(description = it.toString())
        }

        viewModel.observeFillingForm().observe(viewLifecycleOwner) { state ->
            binding.playlistNameInputEditText.setText(state.name)
            binding.playlistDescriptionInputEditText.setText(state.description)
            Glide.with(this)
                .load(state.im)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, requireContext())))
                .into(binding.addPhotoIm)
            viewModel.updateState(
                im = state.im,
                name = state.name,
                description = state.description
            )
        }
        viewModel.observeFormState().observe(viewLifecycleOwner) { state ->
            binding.createPlaylistButton.isEnabled = state.createButtonEnabled
        }


        binding.createPlaylistButton.setOnClickListener {
            viewModel.updatePlaylist()
        }

        viewModel.closeScreenEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
            Toast.makeText(
                context,
                getString(
                    R.string.new_playlist_update_message,
                    binding.playlistNameInputEditText.text
                ),
                Toast.LENGTH_SHORT
            ).show()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARTWORK_RADIUS = 8f
        private const val ARGS_PLAYLIST = "playlistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST to playlistId)
    }
}

