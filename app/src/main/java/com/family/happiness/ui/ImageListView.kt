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

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.ImageListView,
            0, 0
        ).apply {
            try {
                imageMargin = getDimension(R.styleable.ImageListView_image_margin, dpToPx(context, 2f)).toInt()
                imageWidth = getDimension(R.styleable.ImageListView_image_width, dpToPx(context, 2f)).toInt()
                imageHeight = getDimension(R.styleable.ImageListView_image_height, dpToPx(context, 2f)).toInt()
                shapeAppearanceOverlay = getResourceId(
                    R.styleable.ImageListView_image_shape_appearance_overlay,
                    R.style.circleImageView
                )
            } finally {
                recycle()
            }
        }
    }

    fun setImageUrls(urls: List<String>?) {
        urls?.forEach {
            binding.linearLayout.addView(
                (DefaultShapeableImageBinding.inflate(inflater, binding.linearLayout, false)
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