package com.family.happiness.ui.mailwrite

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.WriteMailData
import com.family.happiness.repository.MailRepository
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch

class MailWriteViewModel(
    private val userRepository: UserRepository,
    private val mailRepository: MailRepository
) : ViewModel() {

//    val members = userRepository.members.asLiveData()
    // TODO delete after test
    val members = userRepository.users.asLiveData()

    private val _inputEnabled = MutableLiveData(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _mailSendFlag = MutableLiveData<Flag<Boolean>>()
    val mailSendFlag: LiveData<Flag<Boolean>> = _mailSendFlag

    fun writeMail(toUserId: String, content: String) = viewModelScope.launch {
        _inputEnabled.value = false
        when (val resource = mailRepository.writeMail(WriteMailData(toUserId, content))) {
            is SafeResource.Success -> { _mailSendFlag.value = Flag(true) }
            is SafeResource.Failure -> { _mailSendFlag.value = Flag(false) }
        }
        _inputEnabled.value = true
    }
}