package com.family.happiness.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.MailRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MailViewModel(mailRepository: MailRepository): ViewModel() {

    val mails = mailRepository.mails.asLiveData()

    init {
        viewModelScope.launch {
            when(val resource = mailRepository.syncMail()){
                is SafeResource.Failure -> {Timber.d(resource.throwable)}
            }
        }
    }
}