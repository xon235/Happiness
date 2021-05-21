package com.family.happiness.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.family.happiness.adapter.AlbumListAdapter
import com.family.happiness.adapter.PhotoListAdapter
import com.family.happiness.R
import com.family.happiness.databinding.FragmentAlbumBinding
import com.family.happiness.ui.HappinessBaseFragment


class AlbumFragment : HappinessBaseFragment<FragmentAlbumBinding, AlbumViewModel>() {

    companion object {
        private const val PICK_IMAGE = 101
    }

        private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
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

        binding.imageButton.setOnClickListener {
            viewModel.showAlbumItems.value = null
        }

        photoListAdapter = setupImageGrid()
        setupAlbumGrid(photoListAdapter)
    }

    private fun setupImageGrid(): PhotoListAdapter {
        val imageListAdapter = PhotoListAdapter { viewModel.displayPropertyDetails(it) }
        binding.imageRecyclerView.adapter = imageListAdapter

        viewModel.photos.observe(viewLifecycleOwner) {
            if (viewModel.showAlbumItems.value != null) {
                val filteredImages = viewModel.photos.value?.filter { image ->
                    image.eventId == viewModel.showAlbumItems.value
                }
                imageListAdapter.submitList(filteredImages)
            } else {
                imageListAdapter.submitList(it)
            }
        }

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) {
            if (null != it) {
                findNavController().navigate(
                    AlbumFragmentDirections.actionAlbumFragmentToDetailViewFragment(
                        it
                    )
                )
                viewModel.displayPropertyDetailsComplete()
            }
        }

        return imageListAdapter
    }

    private fun setupAlbumGrid(photoListAdapter: PhotoListAdapter) {
        val albumListAdapter = AlbumListAdapter { album ->
            val filteredImages = viewModel.photos.value?.filter { image ->
                image.eventId == album
            }
            photoListAdapter.submitList(filteredImages)
            viewModel.showAlbumItems.value = album
        }
        binding.albumRecyclerView.adapter = albumListAdapter

        viewModel.events.observe(viewLifecycleOwner) {
            albumListAdapter.submitList(it.map { event -> event.name })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        sharedPreferences.getBoolean(getString(R.string.view_album_in_images), true).also {
            viewModel.viewAlbumInPhotos.value = it
        }

        viewModel.viewAlbumInPhotos.observe(viewLifecycleOwner) {
            menu.findItem(R.id.image).isVisible = !it
            menu.findItem(R.id.folder).isVisible = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.folder -> {
                item.isVisible = false
                viewModel.viewAlbumInPhotos.value = false
                viewModel.showAlbumItems.value = null
            }
            R.id.image -> {
                item.isVisible = false
                viewModel.viewAlbumInPhotos.value = true
                viewModel.showAlbumItems.value = null
                photoListAdapter.submitList(viewModel.photos.value)
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.edit().putBoolean(
            getString(R.string.view_album_in_images),
            viewModel.viewAlbumInPhotos.value!!
        ).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Companion.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
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
                val action =
                    AlbumFragmentDirections.actionAlbumFragmentToUploadImageFragment(uris.toTypedArray())
                findNavController().navigate(action)
            }
        }
    }

    fun onFabClick(view: View) {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(getIntent, Companion.PICK_IMAGE)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAlbumBinding.inflate(inflater, container, false)

    override fun getViewModel() = AlbumViewModel::class.java
}