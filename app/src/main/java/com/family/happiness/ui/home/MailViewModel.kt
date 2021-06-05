package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.MailRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MailViewModel(private val mailRepository: MailRepository): ViewModel() {

    val mails = mailRepository.mails.asLiveData()

    init {
        viewModelScope.launch {
            when(val resource = mailRepository.syncMail()){
                is SafeResource.Success -> {Timber.d("Sync successful")}
                is SafeResource.Failure -> {Timber.d(resource.throwable)}
            }
        }
    }

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _syncFinishFlag = MutableLiveData<Flag<Boolean>>()
    val syncFinishFlag: LiveData<Flag<Boolean>> = _syncFinishFlag

    fun syncMail() = viewModelScope.launch {
        _isRefreshing.value = true
        when(val resource = mailRepository.syncMail()){
            is SafeResource.Success -> {
                _syncFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _syncFinishFlag.value = Flag(false)
            }
        }
        _isRefreshing.value = false
    }
}