package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.EventItemLayoutBinding
import com.family.happiness.room.event.Event

class EventListAdapter(private val clickListener: (event: Event) -> Unit)
    : ListAdapter<Event, EventListAdapter.AlbumViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(EventItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(album)
        }
        holder.bind(album)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    class AlbumViewHolder(private var binding: EventItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.event = event
            binding.executePendingBindings()
        }
    }
}