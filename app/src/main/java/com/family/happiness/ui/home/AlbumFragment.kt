package com.family.happiness.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.family.happiness.adapter.EventListAdapter
import com.family.happiness.adapter.PhotoListAdapter
import com.family.happiness.R
import com.family.happiness.databinding.FragmentAlbumBinding
import com.family.happiness.ui.HappinessBaseFragment


class AlbumFragment : HappinessBaseFragment<FragmentAlbumBinding, AlbumViewModel>() {

    companion object {
        private const val PICK_IMAGE = 101
    }

    private lateinit var photoListAdapter: PhotoListAdapter

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

        binding.albumFragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.photoRecyclerView.adapter =
            PhotoListAdapter { viewModel.displayPropertyDetails(it) }
        binding.eventRecyclerView.adapter =
            EventListAdapter { viewModel.selectedEvent.postValue(it) }

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) { flag ->
            flag?.getContentIfNotHandled()?.let {
                navController.navigate(
                    AlbumFragmentDirections.actionAlbumFragmentToDetailViewFragment(it)
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_menu, menu)

        viewModel.isEventView.observe(viewLifecycleOwner) {
            menu.findItem(R.id.photoView).isVisible = it
            menu.findItem(R.id.eventView).isVisible = !it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.isEventView.value = item.itemId == R.id.eventView
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uris = mutableListOf<Uri>()
                if (data.data == null) {
                    data.clipData?.let {
                        for (i in 0 until it.itemCount) {
                            uris.add(it.getItemAt(i).uri)
                        }
                    }
                } else {
                    uris.add(data.data!!)
                }

                navController.navigate(
                    AlbumFragmentDirections.actionAlbumFragmentToUploadImageFragment(
                        uris.toTypedArray()
                    )
                )
            }
        }
    }

    fun onRootEventClick() {
        viewModel.selectedEvent.value = null
    }

    fun onFabClick() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(getIntent, PICK_IMAGE)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAlbumBinding.inflate(inflater, container, false)

    override fun getViewModel() = AlbumViewModel::class.java
}