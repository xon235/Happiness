package com.family.happiness.ui.mailread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.family.happiness.repository.MailRepository
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class MailReadViewModel(
    private val userRepository: UserRepository,
    private val mailRepository: MailRepository,
) : ViewModel() {

    val me = userRepository.me.asLiveData()
    val mails = mailRepository.mails.asLiveData()
    val fromUser = combine(
        mailRepository.mails,
        userRepository.users
    ) {
            mails, users  ->
        users.find {
            it.id == mails[0].fromUserId
        }
    }.asLiveData()
}