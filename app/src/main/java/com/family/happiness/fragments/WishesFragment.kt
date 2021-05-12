package com.family.happiness.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.family.happiness.R
import com.family.happiness.WishListAdapter
import com.family.happiness.databinding.FragmentAlbumBinding
import com.family.happiness.databinding.FragmentWishesBinding
import com.family.happiness.viewmodels.*
import timber.log.Timber

class WishesFragment : Fragment() {

    private val viewModel: WishesViewModel by viewModels {
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }

    private lateinit var binding: FragmentWishesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWishesBinding.inflate(inflater)
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
        return binding.root
    }

    fun onFabClick(view: View){
        findNavController().navigate(WishesFragmentDirections.actionWishesFragmentToNewWishFragment(null))
    }
}