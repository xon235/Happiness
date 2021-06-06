package com.family.happiness.ui.photodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.family.happiness.databinding.PhotoDetailBottomSheetLayoutBinding
import com.family.happiness.databinding.TagsShapeableImageBinding
import com.family.happiness.room.photo.PhotoDetail
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView

class PhotoDetailBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: PhotoDetailBottomSheetLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PhotoDetailBottomSheetLayoutBinding.inflate(inflater, container, false)
        val photoDetail: PhotoDetail? = arguments?.getParcelable("photoDetail")
        binding.photoDetail = photoDetail
        photoDetail?.tagDetails?.forEach {
            binding.idTagsWrapper.addView(
                TagsShapeableImageBinding.inflate(inflater, binding.idTagsWrapper, true).apply {
                    photoUrl = it.user.photoUrl
                }.root
            )
        }
        return binding.root
    }
}