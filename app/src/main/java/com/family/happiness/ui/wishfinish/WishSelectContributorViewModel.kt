package com.family.happiness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.family.happiness.HappinessRepository

class WishSelectContributorViewModel(private val repository: HappinessRepository): ViewModel() {

}

class WishSelectContributorViewModelFactory(
    private val repository: HappinessRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishSelectContributorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WishSelectContributorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}