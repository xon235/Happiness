package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.ImageItemBinding
import com.family.happiness.room.Image

class ImageListAdapter(private val clickListener: (image: Image) -> Unit)
    : ListAdapter<Image, ImageListAdapter.ImageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(image)
        }
        holder.bind(image)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.url == newItem.url
        }
    }

    class ImageViewHolder(private var binding: ImageItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            binding.image = image
            binding.executePendingBindings()
        }
    }
}