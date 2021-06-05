package com.family.happiness.ui.mailread

import androidx.lifecycle.*
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

    private val  _markedAsRead = MutableLiveData(false)
    val markedAsRead: LiveData<Boolean> = _markedAsRead

    fun markAsRead(mailId: Int, rating: Float) = CoroutineScope(Dispatchers.IO).launch {
        when (val resource =
            mailRepository.markAsRead((MarkAsReadData(mailId, rating)))) {
            is SafeResource.Success -> {
                Timber.d("Mark as read success")
                _markedAsRead.postValue(true)
            }
            is SafeResource.Failure -> Timber.d(resource.throwable)
        }
    }
}