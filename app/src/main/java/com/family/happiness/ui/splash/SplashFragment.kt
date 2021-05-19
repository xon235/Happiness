package com.family.happiness.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.family.happiness.databinding.FragmentSplashBinding
import com.family.happiness.ui.HappinessBaseFragment

class SplashFragment : HappinessBaseFragment<FragmentSplashBinding, SplashViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.personalData.observe(viewLifecycleOwner) {
            if (it.token == null) {
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
            } else {
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToMailFragment())
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSplashBinding.inflate(inflater, container, false)

    override fun getViewModel() = SplashViewModel::class.java
}