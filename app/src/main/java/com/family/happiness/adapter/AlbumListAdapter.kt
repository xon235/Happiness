package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.family.happiness.R
import com.family.happiness.databinding.EventItemLayoutBinding
import com.family.happiness.databinding.PhotoItemLayoutBinding
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo

class AlbumListAdapter(private val clickListener: (albumItem: AlbumItem) -> Unit) :
    ListAdapter<AlbumListAdapter.AlbumItem, AlbumListAdapter.AlbumItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumItemViewHolder {
        return when (viewType) {
            R.layout.photo_item_layout -> AlbumItemViewHolder.PhotoItemViewHolder(
                PhotoItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
            )
            R.layout.event_item_layout -> AlbumItemViewHolder.EventItemViewHolder(
                EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
            )
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: AlbumItemViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is AlbumItemViewHolder.PhotoItemViewHolder -> {
                holder.bind(item as AlbumItem.PhotoItem)
            }
            is AlbumItemViewHolder.EventItemViewHolder -> {
                holder.bind(item as AlbumItem.EventItem)
            }
        }

        holder.itemView.setOnClickListener {
            clickListener(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlbumItem.PhotoItem -> R.layout.photo_item_layout
            is AlbumItem.EventItem -> R.layout.event_item_layout
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AlbumItem>() {
        override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class AlbumItem(val item: Any) {
        data class PhotoItem(val photo: Photo) : AlbumItem(photo)
        data class EventItem(val event: Event) : AlbumItem(event)

        override fun equals(other: Any?): Boolean {
            return (this as? PhotoItem == other as? PhotoItem
                    && this as? EventItem == other as? EventItem)
        }

        override fun hashCode(): Int {
            return item.hashCode()
        }
    }

    sealed class AlbumItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        class PhotoItemViewHolder(private val binding: PhotoItemLayoutBinding) :
            AlbumItemViewHolder(binding) {
            fun bind(photoItem: AlbumItem.PhotoItem) {
                binding.photo = photoItem.photo
                binding.executePendingBindings()
            }
        }

        class EventItemViewHolder(private val binding: EventItemLayoutBinding) :
            AlbumItemViewHolder(binding) {
            fun bind(eventItem: AlbumItem.EventItem) {
                binding.event = eventItem.event
                binding.executePendingBindings()
            }
        }
    }
}