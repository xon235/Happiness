package com.family.happiness.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.family.happiness.R
import com.family.happiness.adapter.WishListAdapter
import com.family.happiness.databinding.FragmentWishBinding
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class WishFragment : HappinessBaseFragment<FragmentWishBinding, WishViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishesFragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.swipeRefreshLayout.setOnRefreshListener { refresh() }

        binding.recyclerView.adapter = WishListAdapter {
            findNavController().navigate(
                WishFragmentDirections.actionWishFragmentToWishDetailFragment(
                    it
                )
            )
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.syncFinishFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Refresh ${if (it) "successful" else "failed"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wish_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                refresh()
            }
        }

        return true
    }

    private fun refresh() {
        viewModel.syncWish()
    }

    fun onFabClick() {
        findNavController().navigate(
            WishFragmentDirections.actionWishFragmentToWishWriteFragment(
                null
            )
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishBinding.inflate(inflater, container, false)

    override fun getViewModel() = WishViewModel::class.java
}