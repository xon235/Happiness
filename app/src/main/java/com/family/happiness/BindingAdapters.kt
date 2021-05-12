package com.family.happiness

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.rooms.Image
import com.family.happiness.rooms.Member
import com.family.happiness.rooms.Wish

@BindingAdapter("app:goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:enabledUnless")
fun enabledUnless(view: View, isEnabled: Boolean) {
    view.isEnabled = isEnabled
}

@BindingAdapter("app:imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if(imgUrl != null){
        Glide.with(imgView.context).load(imgUrl).placeholder(R.drawable.loading_animation).error(R.drawable.ic_connection_error).into(imgView)
    }
}

@BindingAdapter("app:listData")
fun bindMembersRecyclerView(recyclerView: RecyclerView,
                     data: List<Member>?) {
    val adapter = recyclerView.adapter as FamilyListAdapter
    adapter.submitList(data)
}

@BindingAdapter("app:listData")
fun bindImagesRecyclerView(recyclerView: RecyclerView,
                     data: List<Image>?) {
    val adapter = recyclerView.adapter as ImageListAdapter
    adapter.submitList(data)
}

@BindingAdapter("app:listData")
fun bindWishesRecyclerView(recyclerView: RecyclerView,
                           data: List<Wish>?) {
    val adapter = recyclerView.adapter as WishListAdapter
    adapter.submitList(data)
}

@BindingAdapter("HappinessApiStatus")
fun bindStatus(statusImageView: ImageView,
               status: HappinessApiStatus?) {
    when (status) {
        HappinessApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        HappinessApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        HappinessApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}