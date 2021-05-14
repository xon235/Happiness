package com.family.happiness.ui.mailwrite

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.family.happiness.databinding.FragmentWriteMailBinding
import com.family.happiness.room.Member
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class WriteMailFragment : HappinessBaseFragment() {

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