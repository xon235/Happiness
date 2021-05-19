package com.family.happiness.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.family.happiness.R
import com.family.happiness.databinding.FragmentSmsVerificationBinding
import com.family.happiness.network.HappinessApiService
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class SmsVerificationFragment : HappinessBaseFragment<FragmentSmsVerificationBinding, SmsVerificationViewModel>() {

    val args: SmsVerificationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView2.text = "Sent to: ${args.phone}"

        binding.button.setOnClickListener {
//            it.isEnabled = false
//            lifecycleScope.launch {
//                val tokenData = args.accountInfo.toTokenData()
//                val profileData = ProfileData(
//                    args.accountInfo.name,
//                    args.phone,
//                    args.accountInfo.photoUrl
//                )
//                signUp(tokenData, profileData, binding.editTextNumber.text.toString())
//            }
        }

//        happinessViewModel.user.observe(viewLifecycleOwner){
//            if(it != null){
//                findNavController().navigate(R.id.action_global_mailFragment)
//            }
//        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSmsVerificationBinding.inflate(inflater, container, false)

    override fun getViewModel() = SmsVerificationViewModel::class.java

//    private suspend fun signUp(tokenData: TokenData, profileData: ProfileData, smsCode: String) {
//        try {
//            val response = HappinessApiService.retrofitService.signUp(SignUpData(tokenData, profileData, smsCode))
//            if(response.result) {
//                happinessViewModel.newUser(response.user!!)
//            } else {
//                binding.textView3.visibility = View.VISIBLE
//                binding.textView3.text = response.message
//            }
//        } catch (e: Exception){
//            Timber.d(e)
//            binding.textView3.visibility = View.VISIBLE
//            binding.textView3.text = getString(R.string.server_failed_please_try_again)
//        } finally {
//            binding.button.isEnabled = true
//        }
//    }
}