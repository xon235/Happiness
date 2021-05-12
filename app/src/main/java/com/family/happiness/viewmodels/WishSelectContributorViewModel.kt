package com.family.happiness.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.family.happiness.HappinessRepository
import com.family.happiness.rooms.Member
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber

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