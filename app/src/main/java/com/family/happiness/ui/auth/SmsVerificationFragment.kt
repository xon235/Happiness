package com.family.happiness.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.family.happiness.databinding.FragmentSmsVerificationBinding
import com.family.happiness.network.request.SignUpData
import com.family.happiness.ui.HappinessBaseFragment

class SmsVerificationFragment :
    HappinessBaseFragment<FragmentSmsVerificationBinding, SmsVerificationViewModel>() {

    private val args: SmsVerificationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.smsVerificationFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.phoneTextView.text = "Sent to: ${args.phone}"

        viewModel.personalData.observe(viewLifecycleOwner) {
            if (it.token != null) {
                navController.navigate(SmsVerificationFragmentDirections.actionSmsVerificationFragmentToMailFragment())
                viewModel.setToDefault()
            }
        }
    }

    fun onClickFinish() {
        viewModel.signUp(
            SignUpData(args.oAuthData, args.phone, binding.codeEditText.text.toString())
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSmsVerificationBinding.inflate(inflater, container, false)

    override fun getViewModel() = SmsVerificationViewModel::class.java
}