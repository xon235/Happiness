package com.family.happiness.ui.createfamily

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.family.happiness.Utils
import com.family.happiness.databinding.FragmentCreateFamilyBinding
import com.family.happiness.ui.HappinessBaseFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


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

        viewModel.personalData.observe(viewLifecycleOwner){
            if(it.familyId != null) {
                binding.familyIdImageView.setImageBitmap(Utils.encodeAsBitmap(it.familyId))
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