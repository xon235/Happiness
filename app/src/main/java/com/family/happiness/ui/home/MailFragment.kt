package com.family.happiness.ui.home

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.family.happiness.databinding.FragmentMailBinding
import com.family.happiness.ui.HappinessBaseFragment

class MailFragment : HappinessBaseFragment<FragmentMailBinding, MailViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mailFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    fun onClickRead() {
        findNavController()
            .navigate(MailFragmentDirections.actionMailFragmentToMailReadFragment())
    }

    fun onClickFab(){
        findNavController()
            .navigate(MailFragmentDirections.actionMailFragmentToMailWriteFragment())
    }


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMailBinding.inflate(inflater, container, false)

    override fun getViewModel(): Class<MailViewModel> = MailViewModel::class.java
}