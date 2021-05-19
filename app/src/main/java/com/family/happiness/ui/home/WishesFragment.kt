package com.family.happiness.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.family.happiness.HappinessApplication
import com.family.happiness.adapter.WishListAdapter
import com.family.happiness.databinding.FragmentWishesBinding
import com.family.happiness.ui.HappinessBaseFragment
import com.family.happiness.ui.ViewModelFactory

class WishesFragment : HappinessBaseFragment<FragmentWishesBinding, WishesViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishesFragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = WishListAdapter {
            findNavController().navigate(WishesFragmentDirections.actionWishesFragmentToWishDetailFragment(it))
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    fun onFabClick(view: View){
        findNavController().navigate(WishesFragmentDirections.actionWishesFragmentToNewWishFragment(null))
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishesBinding.inflate(inflater, container, false)

    override fun getViewModel() = WishesViewModel::class.java
}