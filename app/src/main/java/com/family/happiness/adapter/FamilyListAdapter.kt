package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.NavActionLayoutBinding
import com.family.happiness.room.Member
import timber.log.Timber

class FamilyListAdapter()
    : ListAdapter<Member, FamilyListAdapter.FamilyViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyViewHolder {
        return FamilyViewHolder(NavActionLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FamilyViewHolder, position: Int) {
        val familyMember = getItem(position)
        holder.itemView.setOnClickListener {
            Timber.d(familyMember.name)
        }
        holder.bind(familyMember)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class FamilyViewHolder(private var binding:
                                 NavActionLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member) {
            binding.familyMember = member
            binding.executePendingBindings()
        }
    }
}