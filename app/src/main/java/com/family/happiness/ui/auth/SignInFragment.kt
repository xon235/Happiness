package com.family.happiness.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.family.happiness.R
import com.family.happiness.databinding.FragmentSignInBinding
import com.family.happiness.network.request.OAuthData
import com.family.happiness.ui.HappinessBaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class SignInFragment : HappinessBaseFragment<FragmentSignInBinding, SignInViewModel>() {

    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requireActivity().onBackPressedDispatcher.addCallback { requireActivity().finish() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.personalData.observe(viewLifecycleOwner) {
            if (it.token != null) {
                navController.navigate(SignInFragmentDirections.actionSignInFragmentToMailFragment())
            }
        }

        viewModel.oAuthData.observe(viewLifecycleOwner) { oAuthData ->
            if (oAuthData != null) {
                navigateToSignUp(oAuthData)
                viewModel.setToDefault()
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build().also {
                    GoogleSignIn.getClient(requireActivity(), it).signOut()
                }
            }
        }

        binding.signInButton.setOnClickListener {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .build()
                .also { googleSignInOptions ->
                    startActivityForResult(
                        GoogleSignIn.getClient(requireActivity(), googleSignInOptions).signInIntent,
                        RC_SIGN_IN
                    )
                    viewModel.disableInput()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    val oAuthData = OAuthData(
                        "google",
                        account.idToken!!,
                        account.displayName!!,
                        account.photoUrl.toString()
                    )
                    viewModel.signIn(oAuthData)
                }
            } catch (e: ApiException) {
                viewModel.setFailUi("Google sign in failed.")
            }
        }
    }

    private fun navigateToSignUp(OAuthData: OAuthData) {
        navController.navigate(
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment(OAuthData)
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignInBinding.inflate(inflater, container, false)

    override fun getViewModel() = SignInViewModel::class.java
}