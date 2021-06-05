package com.family.happiness.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.family.happiness.R
import com.family.happiness.databinding.FragmentMailBinding
import com.family.happiness.ui.HappinessBaseFragment

class MailFragment : HappinessBaseFragment<FragmentMailBinding, MailViewModel>() {

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

        binding.mailFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.swipeRefreshLayout.setOnRefreshListener { refresh() }

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
        inflater.inflate(R.menu.mail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> { refresh() }
        }

        return true
    }

    private fun refresh() {
        viewModel.syncMail()
    }

    fun onClickRead() {
        if(viewModel.mailDetails.value?.isNotEmpty() == true){
            findNavController()
                .navigate(MailFragmentDirections.actionMailFragmentToMailReadFragment(viewModel.mailDetails.value!![0]))
        }
    }

    fun onClickFab(){
        findNavController()
            .navigate(MailFragmentDirections.actionMailFragmentToMailWriteFragment())
    }


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMailBinding.inflate(inflater, container, false)

    override fun getViewModel(): Class<MailViewModel> = MailViewModel::class.java
}