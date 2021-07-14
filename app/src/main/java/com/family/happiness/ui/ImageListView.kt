package com.family.happiness.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.HorizontalScrollView
import androidx.core.view.setMargins
import com.family.happiness.R
import com.family.happiness.Utils.dpToPx
import com.family.happiness.databinding.DefaultShapeableImageBinding
import com.family.happiness.databinding.ImageListViewLayoutBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

class ImageListView(context: Context, attrs: AttributeSet? = null) :
    HorizontalScrollView(context, attrs) {

    private val inflater by lazy { LayoutInflater.from(context) }
    private val binding by lazy { ImageListViewLayoutBinding.inflate(inflater, this, true) }

    private var imageMargin: Int
    private var imageWidth: Int
    private var imageHeight: Int
    private var shapeAppearanceOverlay: Int

    companion object {
        const val DEFAULT_IMAGE_MARGIN_DP = 2f
        const val DEFAULT_IMAGE_WIDTH_DP = 32f
        const val DEFAULT_IMAGE_HEIGHT_DP = 32f
        const val DEFAULT_SHAPE_APPEARANCE_OVERLAY_RESOURCE_ID = R.style.circleImageView
    }

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.ImageListView,
            0, 0
        ).apply {
            try {
                imageMargin = getDimension(R.styleable.ImageListView_image_margin, dpToPx(context, DEFAULT_IMAGE_MARGIN_DP)).toInt()
                imageWidth = getDimension(R.styleable.ImageListView_image_width, dpToPx(context, DEFAULT_IMAGE_WIDTH_DP)).toInt()
                imageHeight = getDimension(R.styleable.ImageListView_image_height, dpToPx(context, DEFAULT_IMAGE_HEIGHT_DP)).toInt()
                shapeAppearanceOverlay = getResourceId(
                    R.styleable.ImageListView_image_shape_appearance_overlay,
                    DEFAULT_SHAPE_APPEARANCE_OVERLAY_RESOURCE_ID
                )
            } finally {
                recycle()
            }
        }
    }

    fun setImageUrls(urls: List<String>?) {
        binding.root.removeAllViews()
        urls?.forEach {
            binding.root.addView(
                (DefaultShapeableImageBinding.inflate(inflater, binding.root, false)
                    .apply { photoUrl = it }.root as ShapeableImageView).apply {
                    layoutParams =
                        MarginLayoutParams(imageWidth, imageHeight).apply {
                            setMargins(imageMargin)
                        }
                    shapeAppearanceModel = ShapeAppearanceModel.builder(
                        context,
                        shapeAppearanceOverlay,
                        shapeAppearanceOverlay
                    ).build()
                }
            )
        }
    }
}