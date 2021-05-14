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


class SignUpFragment : HappinessBaseFragment() {

    lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }
    private val args: SignUpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.accountInfo = args.accountInfo

        viewModel.navigateToSmsVerification.observe(viewLifecycleOwner){
            if(it == true){
                navigateToSmsVerification()
                viewModel.setToDefault()
            }
        }

        return binding.root
    }

    private fun navigateToSmsVerification(){
        val action = SignUpFragmentDirections.actionSignUpFragmentToSmsVerificationFragment(
            viewModel.phone,
            args.accountInfo
        )
        findNavController().navigate(action)
    }
}