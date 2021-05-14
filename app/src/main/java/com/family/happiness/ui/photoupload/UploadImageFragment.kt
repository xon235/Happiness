package com.family.happiness.ui.photoupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.family.happiness.HappinessApplication
import com.family.happiness.network.PhotoUploadBody
import com.family.happiness.adapter.TagListAdapter
import com.family.happiness.databinding.FragmentUploadImageBinding
import com.family.happiness.room.Member
import com.family.happiness.room.toMember
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.ui.ViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class UploadImageFragment : HappinessBaseFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentUploadImageBinding
    private val args: UploadImageFragmentArgs by navArgs()
    val viewModel: UploadImageViewModel by viewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }

    private lateinit var members: List<Member>
    private val taggedList = mutableListOf<Member>()

    private lateinit var albums: List<String>
    private lateinit var selectedAlbum: String

    private var isNewAlbum = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initPhotoLayout()

        binding.albumSpinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
        binding.albumSpinner.onItemSelectedListener = this

        selectedAlbum = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        binding.albumEditText.setText(selectedAlbum)

        val tagListAdapter = TagListAdapter(){isChecked, position ->
            if(isChecked){
                taggedList.add(members[position])
            } else {
                taggedList.remove(members[position])
            }
        }
        binding.recyclerView.adapter = tagListAdapter
        happinessViewModel.members.observe(viewLifecycleOwner){
            members = arrayListOf(happinessViewModel.user.value!!.toMember()) + it
            tagListAdapter.submitList(members)
        }

        binding.button.setOnClickListener {
            val files = args.uris.mapIndexed { index, uri ->
                val parcelFileDescriptor = requireActivity().contentResolver.openFileDescriptor(uri, "r")?: return@setOnClickListener
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(requireContext().cacheDir, inputStream.hashCode().toString())
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                MultipartBody.Part.createFormData(
                    "Photo${index}",
                    file.name,
                    PhotoUploadBody(file, MediaType.parse(requireActivity().contentResolver.getType(uri))!!)
                )
            }
            viewModel.upload(isNewAlbum, selectedAlbum, taggedList, files)
        }

        binding.albumEditText.addTextChangedListener {
            selectedAlbum = it.toString()
        }

        return binding.root
    }

    private fun initPhotoLayout() {
        args.uris.forEach {
            binding.photoLayout.addView(
                ImageView(requireContext()).apply {
                    this.setImageURI(it)
                    this.layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT)
                    this.adjustViewBounds = true
                }
            )
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.albumEditLayout.isVisible = position == 0
        isNewAlbum = position == 0
        if(isNewAlbum){
           selectedAlbum = binding.albumEditText.text.toString()
        } else {
            viewModel.albums.value?.also {
                selectedAlbum = it[position - 1]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.d("onNothingSelected")
    }
}