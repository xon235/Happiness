package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.ImageItemBinding
import com.family.happiness.room.photo.Photo

class PhotoListAdapter(private val clickListener: (image: Photo) -> Unit)
    : ListAdapter<Photo, PhotoListAdapter.ImageViewHolder>(DiffCallback) {

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

    companion object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.url == newItem.url
        }
    }

    class ImageViewHolder(private var binding: ImageItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Photo) {
            binding.photo = image
            binding.executePendingBindings()
        }
    }
}