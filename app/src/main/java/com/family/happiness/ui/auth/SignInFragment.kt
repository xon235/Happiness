package com.family.happiness.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.family.happiness.HappinessApplication
import com.family.happiness.R
import com.family.happiness.databinding.FragmentSignInBinding
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.ui.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import timber.log.Timber


class SignInFragment : HappinessBaseFragment() {

    private val RC_SIGN_IN = 1
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }
    lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.signInButton.setOnClickListener {
            startActivityForResult(viewModel.getSignInIntent(requireActivity()), RC_SIGN_IN)
        }

        happinessViewModel.user.observe(viewLifecycleOwner){
            if(it != null){
                findNavController().navigate(R.id.action_global_mailFragment)
            }
        }

        viewModel.accountInfo.observe(viewLifecycleOwner){
            if(it != null){
                navigateToSignUp(it)
                viewModel.setToDefault(requireActivity())
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)?.let {account ->
                    Timber.d(account.photoUrl.toString())
                    val accountInfo = AccountInfo("google", account.idToken!!, account.displayName!!, account.photoUrl.toString())
                    viewModel.signInWithToken(accountInfo)
                }
            } catch (e: ApiException) {
                viewModel.setFailUi()
            }
        }
    }

    private fun navigateToSignUp(accountInfo: AccountInfo){
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(accountInfo)
        findNavController().navigate(action)
    }
}