package com.family.happiness.ui.mailread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.MarkAsReadData
import com.family.happiness.repository.MailRepository
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

class MailReadViewModel(
    private val userRepository: UserRepository,
    private val mailRepository: MailRepository,
) : ViewModel() {

    val me = userRepository.me.asLiveData()
    val mailDetails = mailRepository.mailDetails.asLiveData()

    fun markAsRead(rating: Float) = CoroutineScope(Dispatchers.IO).launch {
        when (val resource =
            mailRepository.markAsRead((MarkAsReadData(mailDetails.value!![0].mail.id, rating)))) {
            is SafeResource.Success -> Timber.d("Mark as read success")
            is SafeResource.Failure -> Timber.d(resource.throwable)
        }
    }
}