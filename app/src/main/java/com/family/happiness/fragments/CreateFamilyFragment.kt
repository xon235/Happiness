package com.family.happiness.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.family.happiness.HappinessApplication
import com.family.happiness.databinding.FragmentCreateFamilyBinding
import com.family.happiness.viewmodels.CreateFamilyViewModel
import com.family.happiness.viewmodels.ViewModelFactory


class CreateFamilyFragment : HappinessFragment() {

    lateinit var binding: FragmentCreateFamilyBinding
    private val viewModel: CreateFamilyViewModel by viewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateFamilyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.copyButton.setOnClickListener {
            val clipboard = getSystemService(
                requireContext(),
                ClipboardManager::class.java
            )
            val clip = ClipData.newPlainText("Family Code", viewModel.user.value!!.id_family)
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        viewModel.user.observe(viewLifecycleOwner){
            if(it != null && it.id_family == null){
                viewModel.createFamily()
            }
        }

        return binding.root
    }
}