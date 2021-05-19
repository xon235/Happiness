package com.family.happiness.ui.wishfinish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.family.happiness.adapter.TagListAdapter
import com.family.happiness.databinding.FragmentWishFinishBinding
import com.family.happiness.room.user.User
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.viewmodel.WishFinisViewModel
import timber.log.Timber

class WishFinishFragment : HappinessBaseFragment<FragmentWishFinishBinding, WishFinisViewModel>() {

    private lateinit var users: List<User>
    private val tags = mutableListOf<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagListAdapter = TagListAdapter(){isChecked, position ->
            if(isChecked){
                tags.add(users[position])
            } else {
                tags.remove(users[position])
            }
        }
        binding.recyclerView.adapter = tagListAdapter
//        happinessViewModel.members.observe(viewLifecycleOwner){
//            users = arrayListOf(happinessViewModel.user.value!!.toMember()) + it
//            tagListAdapter.submitList(users)
//        }

        binding.button.setOnClickListener {
            Timber.d("click")
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishFinishBinding.inflate(inflater, container, false)

    override fun getViewModel() = WishFinisViewModel::class.java
}