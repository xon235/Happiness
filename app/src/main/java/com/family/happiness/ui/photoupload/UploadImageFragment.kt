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
import com.family.happiness.room.event.Event
import com.family.happiness.room.user.User
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


class UploadImageFragment : HappinessBaseFragment<FragmentUploadImageBinding, UploadImageViewModel>(), AdapterView.OnItemSelectedListener {

    private val args: UploadImageFragmentArgs by navArgs()

    private lateinit var members: List<User>
    private val taggedList = mutableListOf<User>()

    private lateinit var eventNames: List<String>
    private lateinit var selectedEventName: String

    private var isNewAlbum = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initPhotoLayout()

        binding.albumSpinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
        binding.albumSpinner.onItemSelectedListener = this

        selectedEventName = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        binding.albumEditText.setText(selectedEventName)

        val tagListAdapter = TagListAdapter(){isChecked, position ->
            if(isChecked){
                taggedList.add(members[position])
            } else {
                taggedList.remove(members[position])
            }
        }
        binding.recyclerView.adapter = tagListAdapter
//        happinessViewModel.members.observe(viewLifecycleOwner){
//            members = arrayListOf(happinessViewModel.user.value!!.toMember()) + it
//            tagListAdapter.submitList(members)
//        }

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
//            viewModel.upload(isNewAlbum, selectedAlbum, taggedList, files)
        }

        binding.albumEditText.addTextChangedListener {
            selectedEventName = it.toString()
        }
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
            selectedEventName = binding.albumEditText.text.toString()
        } else {
            viewModel.events.value?.also {
                selectedEventName = it[position - 1].name
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.d("onNothingSelected")
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUploadImageBinding.inflate(inflater, container, false)

    override fun getViewModel() = UploadImageViewModel::class.java
}