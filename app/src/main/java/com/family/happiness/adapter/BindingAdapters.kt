package com.family.happiness.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.family.happiness.PreferenceKeys
import com.family.happiness.R
import com.family.happiness.dataStore
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.user.User
import com.family.happiness.room.wish.Wish
import com.family.happiness.room.wish.WishDetail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@BindingAdapter("app:visibleIf")
fun visibleIf(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:enabledIf")
fun enabledIf(view: View, isEnabled: Boolean) {
    view.isEnabled = isEnabled
    if (view is ViewGroup) {
        view.children.forEach {
            enabledIf(it, isEnabled)
        }
    }
}

// TODO use glide module for global header
@BindingAdapter("app:imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if (imgUrl != null) {
        runBlocking {
            imgView.context.dataStore.data.map { it[PreferenceKeys.TOKEN] }.first()
        }?.let {
            Glide.with(imgView.context).load(
                GlideUrl(
                    imgUrl,
                    LazyHeaders.Builder().addHeader("Authorization", "Bearer $it").build()
                )
            ).placeholder(R.drawable.loading_animation).error(R.drawable.ic_connection_error)
                .into(imgView)
        }
    }
}

@BindingAdapter("app:listData")
fun bindMembersRecyclerView(
    recyclerView: RecyclerView,
    data: List<User>?
) {
    when (val adapter = recyclerView.adapter) {
        is FamilyListAdapter -> adapter.submitList(data)
        is TagListAdapter -> adapter.submitList(data)
    }
}

@BindingAdapter("app:listData")
fun bindPhotoesRecyclerView(
    recyclerView: RecyclerView,
    data: List<Photo>?
) {
    val adapter = recyclerView.adapter as PhotoListAdapter
    adapter.submitList(data)
}

@BindingAdapter("app:listData")
fun bindEventsRecyclerView(
    recyclerView: RecyclerView,
    data: List<Event>?
) {
    val adapter = recyclerView.adapter as EventListAdapter
    adapter.submitList(data)
}

@BindingAdapter("app:listData")
fun bindWishesRecyclerView(
    recyclerView: RecyclerView,
    data: List<WishDetail>?
) {
    val adapter = recyclerView.adapter as WishListAdapter
    adapter.submitList(data)
}


@BindingAdapter("app:listData")
fun bindAlbumSpinner(spinner: Spinner, data: List<Event>?) {
    (spinner.adapter as ArrayAdapter<String>).apply {
        clear()
        add("New Album")
        data?.let { addAll(it.map { event -> event.name }) }
    }
}

@BindingAdapter("app:listData")
fun bindMemberSpinner(spinner: Spinner, data: List<User>?) {
    (spinner.adapter as ArrayAdapter<String>).apply {
        clear()
        data?.let { addAll(it.map { user -> user.name }) }
    }
}

//@BindingAdapter("HappinessApiStatus")
//fun bindStatus(statusImageView: ImageView,
//               status: HappinessApiStatus?) {
//    when (status) {
//        HappinessApiStatus.LOADING -> {
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.loading_animation)
//        }
//        HappinessApiStatus.ERROR -> {
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.ic_connection_error)
//        }
//        HappinessApiStatus.DONE -> {
//            statusImageView.visibility = View.GONE
//        }
//    }
//}