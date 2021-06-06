package com.family.happiness.ui.mailread

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.navArgs
import com.family.happiness.databinding.FragmentMailBinding
import com.family.happiness.databinding.FragmentMailReadBinding
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.ui.MainActivity
import com.family.happiness.ui.wishdetail.WishDetailFragmentArgs
import timber.log.Timber

class MailReadFragment : HappinessBaseFragment<FragmentMailReadBinding, MailReadViewModel>() {

    private val args: MailReadFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mailReadFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.mailDetail = args.mailDetail

        (activity as MainActivity).supportActionBar?.title = args.mailDetail.fromUser.name

        viewModel.markedAsRead.observe(viewLifecycleOwner) {
            if(it) {
                navController.popBackStack()
            }
        }

        (requireActivity() as MainActivity).cancelAllNotification()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMailReadBinding.inflate(inflater, container, false)

    override fun getViewModel() = MailReadViewModel::class.java

    override fun onStop() {
        super.onStop()
        viewModel.markAsRead(args.mailDetail.mail.id, binding.ratingBar.rating)
    }
}