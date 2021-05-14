package com.family.happiness.ui.wishfinish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.family.happiness.adapter.TagListAdapter
import com.family.happiness.databinding.FragmentWishSelectContributorBinding
import com.family.happiness.room.Member
import com.family.happiness.room.toMember
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class WishSelectContributorFragment : HappinessBaseFragment() {

    private lateinit var binding: FragmentWishSelectContributorBinding

    private lateinit var taggableList: List<Member>
    private val taggedList = mutableListOf<Member>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishSelectContributorBinding.inflate(inflater)
        val tagListAdapter = TagListAdapter(){isChecked, position ->
            if(isChecked){
                taggedList.add(taggableList[position])
            } else {
                taggedList.remove(taggableList[position])
            }
        }
        binding.recyclerView.adapter = tagListAdapter
        happinessViewModel.members.observe(viewLifecycleOwner){
            taggableList = arrayListOf(happinessViewModel.user.value!!.toMember()) + it
            tagListAdapter.submitList(taggableList)
        }

        binding.button.setOnClickListener {
            Timber.d("click")
        }

        return binding.root
    }
}