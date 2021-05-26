package com.family.happiness.ui.wishfinish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.family.happiness.adapter.TagListAdapter
import com.family.happiness.databinding.FragmentWishFinishBinding
import com.family.happiness.network.request.FinishWishData
import com.family.happiness.room.user.User
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class WishFinishFragment : HappinessBaseFragment<FragmentWishFinishBinding, WishFinisViewModel>() {

    private val args: WishFinishFragmentArgs by navArgs()
    private val taggedUsers = mutableListOf<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishFinishFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.recyclerView.adapter = TagListAdapter() { isChecked, position ->
            viewModel.users.value?.let {
                if (isChecked) {
                    taggedUsers.add(it[position])
                } else {
                    taggedUsers.remove(it[position])
                }
            }
        }

        viewModel.finishFlag.observe(viewLifecycleOwner){ flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Finish " + if (it) { "successful" } else { "failed" },
                    Toast.LENGTH_SHORT
                ).show()

//                if (it){ navController.popBackStack() }
            }
        }
    }

    fun onClickFinish() {
        viewModel.finishWish(FinishWishData(args.wish.id, contributors = taggedUsers.map { it.id }))
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishFinishBinding.inflate(inflater, container, false)

    override fun getViewModel() = WishFinisViewModel::class.java
}