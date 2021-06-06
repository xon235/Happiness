package com.family.happiness.ui.photodetail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import com.family.happiness.ui.MainActivity
import com.family.happiness.R
import com.family.happiness.databinding.FragmentPhotoDetailBinding
import com.family.happiness.ui.HappinessBaseFragment


class PhotoDetailFragment :
    HappinessBaseFragment<FragmentPhotoDetailBinding, PhotoDetailViewModel>() {

    private val args: PhotoDetailFragmentArgs by navArgs()

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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.setPhoto(args.photo)

        viewModel.photoDetail.observe(viewLifecycleOwner) {
            it?.let {
                (activity as MainActivity).supportActionBar?.title = "/" + it.event.name + "/"
            }
        }

        viewModel.events.observe(viewLifecycleOwner) {}

        viewModel.eventChangedFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    if (it) "Photo moved" else "Move failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.deletePhotoFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    if (it) "Delete successful" else "Delete failed",
                    Toast.LENGTH_SHORT
                ).show()

                if(it){
                    navController.popBackStack()
                }
            }
        }

        viewModel.inputEnabled.observe(viewLifecycleOwner) {
            setHasOptionsMenu(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.moveImage -> {
                viewModel.events.value?.let { events ->
                    AlertDialog.Builder(requireContext())
                        .setTitle("Choose an event")
                        .setItems(events.map { it.name }.toTypedArray()) { dialog, which ->
                            viewModel.changeEvent(
                                viewModel.photoDetail.value!!.photo,
                                events[which]
                            )
                            dialog.dismiss()
                        }.show()
                }
            }
            R.id.deleteImage -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete this image?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deletePhoto(viewModel.photoDetail.value!!.photo)
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                    .show()
            }
        }
        return true
    }

    fun onClickInfo() {
        PhotoDetailBottomSheet().apply {
            arguments = Bundle().apply { putParcelable("photoDetail", viewModel.photoDetail.value) }
            show(requireActivity().supportFragmentManager, "photoDetailBottomSheet")
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPhotoDetailBinding.inflate(inflater, container, false)

    override fun getViewModel() = PhotoDetailViewModel::class.java
}