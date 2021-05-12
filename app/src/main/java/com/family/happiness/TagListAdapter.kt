package com.family.happiness

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.TagListItemLayoutBinding
import com.family.happiness.rooms.Member

class TagListAdapter(private val checkedChangeListener: (isChecked: Boolean, position: Int) -> Unit)
    : ListAdapter<Member, TagListAdapter.TagListViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListAdapter.TagListViewHolder {
        return TagListViewHolder(TagListItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
        holder.bind(getItem(position), position, checkedChangeListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class TagListViewHolder(private var binding:
                           TagListItemLayoutBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member, position: Int, checkedChangeListener: (isChecked: Boolean, position: Int) -> Unit) {
            binding.member = member
            binding.executePendingBindings()
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener(isChecked, position)
            }
        }
    }
}