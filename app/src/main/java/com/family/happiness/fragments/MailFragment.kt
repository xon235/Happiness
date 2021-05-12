package com.family.happiness.fragments

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.family.happiness.R
import com.family.happiness.databinding.FragmentMailBinding

class MailFragment : HappinessFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMailBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(MailFragmentDirections.actionMailFragmentToWriteMailFragment())
        }

        return binding.root
    }
}