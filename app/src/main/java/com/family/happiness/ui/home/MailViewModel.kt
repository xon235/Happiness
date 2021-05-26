package com.family.happiness.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.family.happiness.repository.MailRepository

class MailViewModel(mailRepository: MailRepository): ViewModel() {

    val mails = mailRepository.mails.asLiveData()
}