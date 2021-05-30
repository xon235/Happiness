package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.PhotoItemLayoutBinding
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDetail

class PhotoListAdapter(private val clickListener: (photo: Photo) -> Unit)
    : ListAdapter<Photo, PhotoListAdapter.ImageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(PhotoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val photo = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(photo)
        }
        holder.bind(photo)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.url == newItem.url
        }
    }

    class ImageViewHolder(private var binding: PhotoItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.photo = photo
            binding.executePendingBindings()
        }
    }
}