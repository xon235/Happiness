package com.family.happiness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import com.family.happiness.ProgressRequestBody
import com.family.happiness.TagListAdapter
import com.family.happiness.databinding.FragmentWishSelectContributorBinding
import com.family.happiness.rooms.Member
import com.family.happiness.rooms.toMember
import okhttp3.MediaType
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.FileInputStream

class WishSelectContributorFragment : HappinessFragment() {

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