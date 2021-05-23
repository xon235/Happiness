package com.family.happiness.ui.photoupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import com.family.happiness.adapter.TagListAdapter
import com.family.happiness.databinding.FragmentPhotoUploadBinding
import com.family.happiness.network.PhotoUploadBody
import com.family.happiness.room.user.User
import com.family.happiness.ui.HappinessBaseFragment
import okhttp3.MediaType
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PhotoUploadFragment :
    HappinessBaseFragment<FragmentPhotoUploadBinding, PhotoUploadViewModel>(),
    AdapterView.OnItemSelectedListener {

    private val args: PhotoUploadFragmentArgs by navArgs()
    private val taggedUsers = mutableListOf<User>()
    private var isNewEvent = false
    private lateinit var eventName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadImageFragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initPhotoLayout()

        binding.eventSpinner.adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
        binding.eventSpinner.onItemSelectedListener = this

        eventName = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        binding.eventEditText.setText(eventName)
        binding.eventEditText.addTextChangedListener { eventName = it.toString() }

        binding.recyclerView.adapter = TagListAdapter() { isChecked, position ->
            viewModel.users.value?.let {
                if (isChecked) {
                    taggedUsers.add(it[position])
                } else {
                    taggedUsers.remove(it[position])
                }
            }
        }

        viewModel.uploadFinishFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Upload ${if(it) "successful" else "failed"}",
                    Toast.LENGTH_SHORT
                ).show()
                if(it){
                    navController.popBackStack()
                }
            }
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
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    this.adjustViewBounds = true
                }
            )
        }
    }

    fun onClickUpload() {
        val parts = args.uris.mapIndexed { index, uri ->
            val parcelFileDescriptor =
                requireActivity().contentResolver.openFileDescriptor(uri, "r")
                    ?: return@onClickUpload
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(requireContext().cacheDir, inputStream.hashCode().toString())
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            MultipartBody.Part.createFormData(
                "Photo${index}",
                file.name,
                PhotoUploadBody(
                    file,
                    MediaType.parse(requireActivity().contentResolver.getType(uri))!!
                )
            )
        }
        viewModel.upload(isNewEvent, eventName, taggedUsers.map { it.id }, parts)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        isNewEvent = position == 0
        binding.eventEditLayout.isVisible = isNewEvent
        if (position == 0) {
            eventName = binding.eventEditText.text.toString()
        } else {
            viewModel.events.value?.get(position - 1)?.let { eventName = it.name }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.d("onNothingSelected")
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPhotoUploadBinding.inflate(inflater, container, false)

    override fun getViewModel() = PhotoUploadViewModel::class.java
}