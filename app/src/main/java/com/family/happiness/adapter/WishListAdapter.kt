package com.family.happiness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.family.happiness.databinding.DefaultShapeableImageBinding
import com.family.happiness.databinding.WishItemLayoutBinding
import com.family.happiness.room.wish.WishDetail

class WishListAdapter(private val clickListener: (wish: WishDetail) -> Unit)
    : ListAdapter<WishDetail, WishListAdapter.WishListViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder(WishItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val wishDetail = getItem(position)
        holder.bind(wishDetail)
        holder.itemView.setOnClickListener {
            clickListener(wishDetail)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<WishDetail>() {
        override fun areItemsTheSame(oldItem: WishDetail, newItem: WishDetail): Boolean {
            return oldItem.wish.id == newItem.wish.id
        }

        override fun areContentsTheSame(oldItem: WishDetail, newItem: WishDetail): Boolean {
            return oldItem == newItem
        }
    }

    class WishListViewHolder(private var binding: WishItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wishDetail: WishDetail) {
            binding.wishDetail = wishDetail
            binding.imageListView.setImageUrls(wishDetail.contributors.map { it.user.photoUrl })
        }
    }
}