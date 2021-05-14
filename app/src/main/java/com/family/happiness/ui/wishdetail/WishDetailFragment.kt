package com.family.happiness.ui.wishdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.R
import com.family.happiness.databinding.FragmentWishDetailBinding

class WishDetailFragment : Fragment() {

    private lateinit var binding: FragmentWishDetailBinding
    val args: WishDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args.wish.timestamp_finish?:setHasOptionsMenu(true)
        binding = FragmentWishDetailBinding.inflate(inflater)
        binding.wish = args.wish
        binding.finishBt.setOnClickListener {
            findNavController().navigate(WishDetailFragmentDirections.actionWishDetailFragmentToWishSelectContributoFragment(args.wish))
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wish_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.modify ->
                findNavController().navigate(WishDetailFragmentDirections.actionWishDetailFragmentToNewWishFragment(args.wish))
        }
        return true
    }
}