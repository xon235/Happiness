package com.family.happiness.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.family.happiness.AlbumListAdapter
import com.family.happiness.HappinessApplication
import com.family.happiness.ImageListAdapter
import com.family.happiness.R
import com.family.happiness.databinding.FragmentAlbumBinding
import com.family.happiness.viewmodels.AlbumViewModel
import com.family.happiness.viewmodels.ViewModelFactory


class AlbumFragment : HappinessFragment() {

    private val PICK_IMAGE = 1

    private val viewModel: AlbumViewModel by viewModels {
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }
    private val sharedPreferences: SharedPreferences by lazy{
        requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        )
    }

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentAlbumBinding.inflate(inflater)
        binding.albumFragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.imageButton.setOnClickListener {
            viewModel.showAlbumItems.value = null
        }

        imageListAdapter = setupImageGrid()
        setupAlbumGrid(imageListAdapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupImageGrid(): ImageListAdapter{
        val imageListAdapter = ImageListAdapter{
            viewModel.displayPropertyDetails(it)
        }
        binding.imageRecyclerView.adapter = imageListAdapter

        viewModel.images.observe(viewLifecycleOwner){
            if(viewModel.showAlbumItems.value != null){
                val filteredImages = viewModel.images.value?.filter {image ->
                    image.album == viewModel.showAlbumItems.value
                }
                imageListAdapter.submitList(filteredImages)
            } else {
                imageListAdapter.submitList(it)
            }
        }

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) {
            if ( null != it ) {
                findNavController().navigate(AlbumFragmentDirections.actionAlbumFragmentToDetailViewFragment(it))
                viewModel.displayPropertyDetailsComplete()
            }
        }

        return imageListAdapter
    }

    private fun setupAlbumGrid(imageListAdapter: ImageListAdapter){
        val albumListAdapter = AlbumListAdapter { album->
            val filteredImages = viewModel.images.value?.filter {image ->
                image.album == album
            }
            imageListAdapter.submitList(filteredImages)
            viewModel.showAlbumItems.value = album
        }
        binding.albumRecyclerView.adapter = albumListAdapter

        viewModel.albums.observe(viewLifecycleOwner){
            albumListAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.album_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        sharedPreferences.getBoolean(getString(R.string.view_album_in_images), true).also {
            viewModel.viewAlbumInImages.value = it
        }

        viewModel.viewAlbumInImages.observe(viewLifecycleOwner){
            menu.findItem(R.id.image).isVisible = !it
            menu.findItem(R.id.folder).isVisible = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.folder -> {
                item.isVisible = false
                viewModel.viewAlbumInImages.value = false
                viewModel.showAlbumItems.value = null
            }
            R.id.image -> {
                item.isVisible = false
                viewModel.viewAlbumInImages.value = true
                viewModel.showAlbumItems.value = null
                imageListAdapter.submitList(viewModel.images.value)
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.edit().putBoolean(
                getString(R.string.view_album_in_images),
                viewModel.viewAlbumInImages.value!!
        ).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            if(data != null){
                val uris = mutableListOf<Uri>()
                if(data.data == null){
                    data.clipData?.let{
                        for(i in 0 until it.itemCount){
                            uris.add(it.getItemAt(i).uri)
                        }
                    }
                } else{
                    uris.add(data.data!!)
                }
                val action = AlbumFragmentDirections.actionAlbumFragmentToUploadImageFragment(uris.toTypedArray())
                findNavController().navigate(action)
            }
        }
    }

    fun onFabClick(view: View){
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(getIntent, PICK_IMAGE)
    }
}