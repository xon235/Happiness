package com.family.happiness.ui.wishnwrite

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.family.happiness.R
import com.family.happiness.databinding.FragmentWishWriteBinding
import com.family.happiness.network.request.DeleteWishData
import com.family.happiness.network.request.WriteWishData
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class WishWriteFragment : HappinessBaseFragment<FragmentWishWriteBinding, WishWriteViewModel>() {

    private val args: WishWriteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishWriteFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.wishDetail = args.wishDetail

        args.wishDetail?.run { setHasOptionsMenu(true) }

        viewModel.writeFinishFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    if (args.wishDetail == null) {
                        "Post"
                    } else {
                        "Update"
                    }
                            + " "
                            + if (it) {
                        "successful"
                    } else {
                        "failed"
                    },
                    Toast.LENGTH_SHORT
                ).show()

                if (it){ navController.navigate(WishWriteFragmentDirections.actionWishWriteFragmentToWishFragment()) }
            }
        }

        viewModel.deleteFinishFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Delete " + if (it) { "successful" } else { "failed" },
                    Toast.LENGTH_SHORT
                ).show()

                if (it){ navController.navigate(WishWriteFragmentDirections.actionWishWriteFragmentToWishFragment()) }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_wish_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete this wish?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteWish(DeleteWishData(args.wishDetail!!.wish.id))
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                    .show()
        }
        return true
    }

    fun onClickFinish() {
        viewModel.writeWish(
            WriteWishData(
                args.wishDetail?.wish?.id,
                binding.titleEt.text.toString(),
                binding.contentEt.text.toString()
            )
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWishWriteBinding.inflate(inflater, container, false)

    override fun getViewModel(): Class<WishWriteViewModel> = WishWriteViewModel::class.java
}