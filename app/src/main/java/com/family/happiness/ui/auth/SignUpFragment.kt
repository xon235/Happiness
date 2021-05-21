package com.family.happiness.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.family.happiness.databinding.FragmentSignUpBinding
import com.family.happiness.network.request.GetSmsData
import com.family.happiness.ui.HappinessBaseFragment


class SignUpFragment : HappinessBaseFragment<FragmentSignUpBinding, SignUpViewModel>() {

    private val args: SignUpFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.oAuthData = args.oAuthData

        viewModel.navigateToSmsVerification.observe(viewLifecycleOwner) {
            if (it == true) {
                navController.navigate(
                    SignUpFragmentDirections.actionSignUpFragmentToSmsVerificationFragment(
                        binding.phoneEditText.text.toString(),
                        args.oAuthData
                    )
                )
                viewModel.setToDefault()
            }
        }
    }

    fun onClickNext(){
        viewModel.getSmsCode(GetSmsData(binding.phoneEditText.text.toString()))
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun getViewModel() = SignUpViewModel::class.java
}