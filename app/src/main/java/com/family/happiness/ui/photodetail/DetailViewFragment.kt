package com.family.happiness.ui.photodetail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.HappinessApplication
import com.family.happiness.ui.MainActivity
import com.family.happiness.R
import com.family.happiness.databinding.FragmentDetailViewBinding
import com.family.happiness.ui.ViewModelFactory


class DetailViewFragment : Fragment() {

    private lateinit var binding: FragmentDetailViewBinding
    private val args: DetailViewFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }

    private lateinit var albums: List<String>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailViewBinding.inflate(inflater)
        viewModel.image.value = args.image
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.albums.observeOnce(viewLifecycleOwner){
            albums = it
        }

        (activity as MainActivity).supportActionBar?.title = "/" + args.image.album + "/"

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.moveImage -> {
                AlertDialog.Builder(requireContext())
                        .setTitle("Choose an album")
                        .setItems(albums.toTypedArray()) { dialog, which ->
                            val newAlbum = albums[which]
                            viewModel.moveImage(args.image, newAlbum)
                            (activity as MainActivity).supportActionBar?.title = "/$newAlbum/"
                            Toast.makeText(requireContext(), "Moved to /$newAlbum/", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }.show()
            }
            R.id.deleteImage -> {
                AlertDialog.Builder(requireContext())
                        .setTitle("Delete this image?")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.deleteImage(args.image)
                            findNavController().popBackStack()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.cancel()
                        }
                        .show()
            }
        }
        return true
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}