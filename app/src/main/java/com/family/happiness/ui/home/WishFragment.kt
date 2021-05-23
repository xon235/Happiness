package com.family.happiness.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.family.happiness.adapter.WishListAdapter
import com.family.happiness.databinding.FragmentWishBinding
import com.family.happiness.ui.HappinessBaseFragment

class WishFragment : HappinessBaseFragment<FragmentWishBinding, WishViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishesFragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = WishListAdapter {
            findNavController().navigate(WishFragmentDirections.actionWishFragmentToWishDetailFragment(it))
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    fun onFabClick(view: View){
        findNavController().navigate(WishFragmentDirections.actionWishFragmentToWishWriteFragment(null))
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishBinding.inflate(inflater, container, false)

    override fun getViewModel() = WishViewModel::class.java
}