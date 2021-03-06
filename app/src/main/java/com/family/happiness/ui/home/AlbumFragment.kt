package com.family.happiness.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.family.happiness.R
import com.family.happiness.adapter.AlbumListAdapter
import com.family.happiness.databinding.FragmentAlbumBinding
import com.family.happiness.ui.HappinessBaseFragment


class AlbumFragment : HappinessBaseFragment<FragmentAlbumBinding, AlbumViewModel>() {

    companion object {
        private const val PICK_IMAGE = 101
    }

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

        val albumListAdapter = AlbumListAdapter {
            when (it) {
                is AlbumListAdapter.AlbumItem.PhotoItem -> {
                    viewModel.displayImageDetails(it.photo)
                }
                is AlbumListAdapter.AlbumItem.EventItem -> {
                    viewModel.setPhotosByEventViewState(it.event)
                }
            }
        }
        binding.albumItemRecyclerView.adapter = albumListAdapter

        binding.swipeRefreshLayout.setOnRefreshListener { refresh() }

        viewModel.navigateToSelectedImage.observe(viewLifecycleOwner) { flag ->
            flag?.getContentIfNotHandled()?.let {
                navController.navigate(
                    AlbumFragmentDirections.actionAlbumFragmentToPhotoDetailFragment(it)
                )
            }
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.syncFinishFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Refresh ${if (it) "successful" else "failed"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.albumItems.observe(viewLifecycleOwner) {
            albumListAdapter.submitList(it)
        }

        viewModel.albumViewState.observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.allPhotosView -> {
                viewModel.setAllPhotosViewState()
            }
            R.id.eventsView -> {
                viewModel.setEventsViewState()
            }
            R.id.refresh -> {
                refresh()
            }

        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val albumViewState = viewModel.albumViewState.value
        menu.findItem(R.id.allPhotosView).isVisible = albumViewState != AlbumViewState.AllPhotos
        menu.findItem(R.id.eventsView).isVisible = albumViewState == AlbumViewState.AllPhotos
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
                    AlbumFragmentDirections.actionAlbumFragmentToPhotoUploadFragment(
                        uris.toTypedArray()
                    )
                )
            }
        }
    }

    private fun refresh() {
        viewModel.syncAlbum()
    }

    fun onClickRootEvent() {
        viewModel.setEventsViewState()
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