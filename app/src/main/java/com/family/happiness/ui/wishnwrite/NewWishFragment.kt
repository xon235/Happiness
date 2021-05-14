package com.family.happiness.ui.wishnwrite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.family.happiness.R
import com.family.happiness.databinding.FragmentNewWishBinding
import timber.log.Timber

class NewWishFragment : Fragment() {

    private lateinit var binding: FragmentNewWishBinding
    val args: NewWishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args.wish?.run{
            setHasOptionsMenu(true)
        }

        binding = FragmentNewWishBinding.inflate(inflater)
        binding.wish = args.wish
        binding.finishBt.setOnClickListener {

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_wish_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete ->
                Timber.d("delete")
        }
        return true
    }
}