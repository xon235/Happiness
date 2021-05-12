package com.family.happiness.fragments

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.MainActivity
import com.family.happiness.R
import com.family.happiness.fragments.WishDetailFragmentArgs
import com.family.happiness.databinding.FragmentAlbumBinding
import com.family.happiness.databinding.FragmentNewWishBinding
import com.family.happiness.databinding.FragmentWishDetailBinding
import com.family.happiness.databinding.FragmentWriteMailBinding
import com.family.happiness.rooms.Member
import timber.log.Timber

class WriteMailFragment : HappinessFragment() {

    private lateinit var binding: FragmentWriteMailBinding
    private lateinit var members: List<Member>
    private var toIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWriteMailBinding.inflate(inflater)
        happinessViewModel.members.observe(viewLifecycleOwner){ members ->
            this.members = members
            binding.spinner.adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                members.map { it.name })
        }
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toIndex = position
                Timber.d(members[position].name)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }
}