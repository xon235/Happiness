package com.family.happiness.ui.photodetail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.ui.MainActivity
import com.family.happiness.R
import com.family.happiness.databinding.FragmentDetailViewBinding
import com.family.happiness.ui.HappinessBaseFragment


class DetailViewFragment : HappinessBaseFragment<FragmentDetailViewBinding, DetailViewModel>() {

    private val args: DetailViewFragmentArgs by navArgs()

    private lateinit var eventNames: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.photo.value = args.photo
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.events.observeOnce(viewLifecycleOwner){
            eventNames = it.map { event -> event.name }
        }

//        (activity as MainActivity).supportActionBar?.title = "/" + args.photo.album + "/"
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
                        .setItems(eventNames.toTypedArray()) { dialog, which ->
                            val newAlbum = eventNames[which]
                            viewModel.moveImage(args.photo, newAlbum)
                            (activity as MainActivity).supportActionBar?.title = "/$newAlbum/"
                            Toast.makeText(requireContext(), "Moved to /$newAlbum/", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }.show()
            }
            R.id.deleteImage -> {
                AlertDialog.Builder(requireContext())
                        .setTitle("Delete this image?")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.deleteImage(args.photo)
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

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDetailViewBinding.inflate(inflater, container, false)

    override fun getViewModel() = DetailViewModel::class.java
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}