package com.family.happiness.ui.mailwrite

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.family.happiness.databinding.FragmentMailWriteBinding
import com.family.happiness.room.user.User
import com.family.happiness.ui.HappinessBaseFragment
import timber.log.Timber

class MailWriteFragment : HappinessBaseFragment<FragmentMailWriteBinding, MailWriteViewModel>() {

    private lateinit var users: List<User>
    private var toIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivityViewModel.users.observe(viewLifecycleOwner){ members ->
            this.users = members
            binding.spinner.adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                members.map { it.name })
        }
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toIndex = position
                Timber.d(users[position].name)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMailWriteBinding.inflate(inflater, container, false)

    override fun getViewModel() = MailWriteViewModel::class.java
}