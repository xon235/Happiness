package com.family.happiness.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.family.happiness.HappinessApplication

abstract class HappinessBaseFragment<B: ViewBinding, V: ViewModel>: Fragment() {

    protected lateinit var binding: B
    protected lateinit var viewModel: V
    // TODO delete when refactoring is finished
    protected val mainActivityViewModel: MainActivityViewModel by activityViewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication))
    }
    protected val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding(inflater, container)
        val factory = ViewModelFactory(requireActivity().application as HappinessApplication)
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        return binding.root
    }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun getViewModel(): Class<V>
}