package com.family.happiness.ui.mailwrite

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.family.happiness.databinding.FragmentMailWriteBinding
import com.family.happiness.room.user.User
import com.family.happiness.ui.HappinessBaseFragment

class MailWriteFragment : HappinessBaseFragment<FragmentMailWriteBinding, MailWriteViewModel>(),
    AdapterView.OnItemSelectedListener {

    private var toUser: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mailWriteFragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.spinner.adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
        binding.spinner.onItemSelectedListener = this

        viewModel.mailSendFlag.observe(viewLifecycleOwner) { flag ->
            flag.getContentIfNotHandled()?.let {
                Toast.makeText(
                    requireContext(),
                    "Mail send ${if(it) "successful" else "failed"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onClickSend() {
        if (toUser == null) {
            Toast.makeText(requireContext(), "Recipient not set", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.writeMail(toUser!!.id, binding.contentEditText.text.toString())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        toUser = viewModel.members.value?.get(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMailWriteBinding.inflate(inflater, container, false)

    override fun getViewModel() = MailWriteViewModel::class.java
}