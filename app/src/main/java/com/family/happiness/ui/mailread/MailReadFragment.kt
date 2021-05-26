package com.family.happiness.ui.mailread

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.family.happiness.databinding.FragmentMailBinding
import com.family.happiness.databinding.FragmentMailReadBinding
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.ui.MainActivity

class MailReadFragment : HappinessBaseFragment<FragmentMailReadBinding, MailReadViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mailReadFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.fromUser.observe(viewLifecycleOwner) {
            (activity as MainActivity).supportActionBar?.title = it?.name
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMailReadBinding.inflate(inflater, container, false)

    override fun getViewModel() = MailReadViewModel::class.java

}