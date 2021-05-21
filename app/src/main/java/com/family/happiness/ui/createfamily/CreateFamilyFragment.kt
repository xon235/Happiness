package com.family.happiness.ui.createfamily

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.family.happiness.databinding.FragmentCreateFamilyBinding
import com.family.happiness.ui.HappinessBaseFragment


class CreateFamilyFragment :
    HappinessBaseFragment<FragmentCreateFamilyBinding, CreateFamilyViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.copyButton.setOnClickListener {
            getSystemService(requireContext(), ClipboardManager::class.java)?.apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        "Family Code", binding.familyIdTextView.text.toString()
                    )
                )
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.createFamily()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCreateFamilyBinding.inflate(inflater, container, false)

    override fun getViewModel() = CreateFamilyViewModel::class.java
}