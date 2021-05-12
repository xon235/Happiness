package com.family.happiness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.family.happiness.HappinessApplication
import com.family.happiness.ProgressRequestBody
import com.family.happiness.TagListAdapter
import com.family.happiness.databinding.FragmentUploadImageBinding
import com.family.happiness.rooms.Member
import com.family.happiness.rooms.toMember
import com.family.happiness.viewmodels.UploadImageViewModel
import com.family.happiness.viewmodels.ViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*


class UploadImageFragment : HappinessFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentUploadImageBinding
    private val args: UploadImageFragmentArgs by navArgs()

    val viewModel: UploadImageViewModel by viewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }

    private lateinit var taggableList: List<Member>
    private val taggedList = mutableListOf<Member>()

    private lateinit var albums: List<String>
    private lateinit var selectedAlbum: String

    private var isNewAlbum = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        args.uris.forEach {
            binding.itemLayout.addView(
                    ImageView(requireContext()).apply {
                        this.setImageURI(it)
                        this.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)
                        this.adjustViewBounds = true
                    }
            )
        }

        viewModel.albums.observe(viewLifecycleOwner){
            albums = listOf("New Album") + it
            binding.spinner.adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                albums)
        }

        val tagListAdapter = TagListAdapter(){isChecked, position ->
            if(isChecked){
                taggedList.add(taggableList[position])
            } else {
                taggedList.remove(taggableList[position])
            }
        }
        binding.recyclerView.adapter = tagListAdapter
        happinessViewModel.members.observe(viewLifecycleOwner){
            taggableList = arrayListOf(happinessViewModel.user.value!!.toMember()) + it
            tagListAdapter.submitList(taggableList)
        }

        binding.spinner.onItemSelectedListener = this
        binding.albumName.setText(
                SimpleDateFormat("yyyy-MM-dd")
                        .format(Calendar.getInstance().time))

        binding.button.setOnClickListener {
            val files = mutableListOf<MultipartBody.Part>()
            args.uris.forEach {
                val fd = requireActivity().contentResolver.openFileDescriptor(it, "r")
                val mediaType = requireActivity().contentResolver.getType(it)
                val input = FileInputStream(fd!!.fileDescriptor)
                val request = ProgressRequestBody(input, MediaType.parse(mediaType)!!)
                files.add(MultipartBody.Part.create(request))
            }
            viewModel.upload(isNewAlbum, selectedAlbum, taggedList, files)
        }

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Timber.d("onItemSelected")
        binding.albumName.isVisible = position == 0
        binding.textView.isVisible = position == 0
        binding.recyclerView.isVisible = position == 0
        if(position == 0){
            isNewAlbum = true
            selectedAlbum = binding.albumName.text.toString()
        } else{
            isNewAlbum = false
            selectedAlbum = albums[position]
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.d("onNothingSelected")
    }
}