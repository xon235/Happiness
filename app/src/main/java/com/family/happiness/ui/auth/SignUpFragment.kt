package com.family.happiness.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.HappinessApplication
import com.family.happiness.databinding.FragmentSignUpBinding
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.ui.ViewModelFactory


class SignUpFragment : HappinessBaseFragment<FragmentSignUpBinding, SignUpViewModel>() {

    private val args: SignUpFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.oAuthData = args.oAuthData

        viewModel.navigateToSmsVerification.observe(viewLifecycleOwner){
            if(it == true){
                navigateToSmsVerification()
                viewModel.setToDefault()
            }
        }
    }

    private fun navigateToSmsVerification(){
        val action = SignUpFragmentDirections.actionSignUpFragmentToSmsVerificationFragment(
            viewModel.phone,
            args.oAuthData
        )
        findNavController().navigate(action)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun getViewModel() = SignUpViewModel::class.java
}