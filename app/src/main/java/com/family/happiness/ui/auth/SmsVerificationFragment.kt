package com.family.happiness.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.R
import com.family.happiness.databinding.FragmentSmsVerificationBinding
import com.family.happiness.network.HappinessApiService
import com.family.happiness.network.ProfileData
import com.family.happiness.network.SignUpData
import com.family.happiness.network.TokenData
import com.family.happiness.ui.HappinessBaseFragment
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class SmsVerificationFragment : HappinessBaseFragment() {

    lateinit var binding: FragmentSmsVerificationBinding
    val args: SmsVerificationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSmsVerificationBinding.inflate(inflater, container, false)

        binding.textView2.text = "Sent to: ${args.phone}"

        binding.button.setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch {
                val tokenData = args.accountInfo.toTokenData()
                val profileData = ProfileData(
                    args.accountInfo.name,
                    args.phone,
                    args.accountInfo.photoUrl
                )
                signUp(tokenData, profileData, binding.editTextNumber.text.toString())
            }
        }

        happinessViewModel.user.observe(viewLifecycleOwner){
            if(it != null){
                findNavController().navigate(R.id.action_global_mailFragment)
            }
        }

        return binding.root
    }

    private suspend fun signUp(tokenData: TokenData, profileData: ProfileData, smsCode: String) {
        try {
            val response = HappinessApiService.retrofitService.signUp(SignUpData(tokenData, profileData, smsCode))
            if(response.result) {
                happinessViewModel.newUser(response.user!!)
            } else {
                binding.textView3.visibility = View.VISIBLE
                binding.textView3.text = response.message
            }
        } catch (e: Exception){
            Timber.d(e)
            binding.textView3.visibility = View.VISIBLE
            binding.textView3.text = getString(R.string.server_failed_please_try_again)
        } finally {
            binding.button.isEnabled = true
        }
    }
}