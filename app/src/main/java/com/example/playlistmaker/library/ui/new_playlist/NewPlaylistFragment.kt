package com.example.playlistmaker.library.ui.new_playlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlaylistCreationBinding
import com.example.playlistmaker.search.ui.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    private val viewModel: NewPlaylistViewModel by viewModel()

    private var _binding: PlaylistCreationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PlaylistCreationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .fitCenter()
                        .transform(RoundedCorners(dpToPx(ARTWORK_RADIUS, requireContext())))
                        .into(binding.addPhotoIm)
                    viewModel.updateState(im = uri)
                } else {
                    Toast.makeText(context, "Вы ничего не выбрали",Toast.LENGTH_SHORT).show()
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

        viewModel.observeFormStateState().observe(viewLifecycleOwner) {
            binding.createPlaylistButton.isEnabled = it.createButtonEnabled
        }
        binding.createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist()
            Toast.makeText(context, "Кажется, вы что-то создали",Toast.LENGTH_SHORT).show()
            //TODO:Поменять текстовки
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val ARTWORK_RADIUS = 8f
    }
}

