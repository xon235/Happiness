package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.TagListItemLayoutBinding
import com.family.happiness.room.user.User

class TagListAdapter(private val checkedChangeListener: (isChecked: Boolean, position: Int) -> Unit)
    : ListAdapter<User, TagListAdapter.TagListViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder {
        return TagListViewHolder(TagListItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
        holder.bind(getItem(position), position, checkedChangeListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class TagListViewHolder(private var binding:
                           TagListItemLayoutBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, position: Int, checkedChangeListener: (isChecked: Boolean, position: Int) -> Unit) {
            binding.user = user
            binding.executePendingBindings()
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener(isChecked, position)
            }
        }
    }
}