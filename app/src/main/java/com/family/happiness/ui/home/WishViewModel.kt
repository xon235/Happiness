package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.WishRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class WishViewModel(private val wishRepository: WishRepository): ViewModel() {
    val wishes = wishRepository.wishDetails.asLiveData()

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _syncFinishFlag = MutableLiveData<Flag<Boolean>>()
    val syncFinishFlag: LiveData<Flag<Boolean>> = _syncFinishFlag

    fun syncWish() = viewModelScope.launch {
        _isRefreshing.value = true
        when(val resource = wishRepository.getWish()){
            is SafeResource.Success -> {
                resource.value.wishes?.let { wishRepository.syncWish(it) }
                resource.value.contributors?.let { wishRepository.syncContributor(it) }
                _syncFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _syncFinishFlag.value = Flag(false)
            }
        }
        _isRefreshing.value = false
    }
}