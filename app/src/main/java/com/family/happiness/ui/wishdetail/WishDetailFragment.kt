package com.family.happiness.ui.wishdetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.family.happiness.R
import com.family.happiness.databinding.FragmentWishDetailBinding
import com.family.happiness.databinding.TagsShapeableImageBinding
import com.family.happiness.ui.HappinessBaseFragment

class WishDetailFragment : HappinessBaseFragment<FragmentWishDetailBinding, WishDetailViewModel>() {

    private val args: WishDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishDetailFragment = this
        binding.wishDetail = args.wishDetail

        args.wishDetail.wish.timestampClose?:setHasOptionsMenu(true)

        args.wishDetail.contributors.forEach {
            binding.tagsWrapper.addView(
                TagsShapeableImageBinding.inflate(layoutInflater, binding.tagsWrapper, false).apply {
                    photoUrl = it.user.photoUrl
                }.root
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.wish_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.modify ->
                findNavController().navigate(WishDetailFragmentDirections.actionWishDetailFragmentToWishWriteFragment(args.wishDetail))
        }
        return true
    }

    fun onClickFinish(){
        findNavController().navigate(WishDetailFragmentDirections.actionWishDetailFragmentToWishFinishFragment(args.wishDetail.wish))
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishDetailBinding.inflate(inflater, container, false)

    override fun getViewModel() = WishDetailViewModel::class.java
}