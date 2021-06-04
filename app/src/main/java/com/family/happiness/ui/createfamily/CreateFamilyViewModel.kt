package com.family.happiness.ui.createfamily

import androidx.lifecycle.*
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.UserRepository
import com.family.happiness.room.user.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CreateFamilyViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _status = MutableLiveData<HappinessApiStatus>(null)
    val status: LiveData<HappinessApiStatus> = _status

    private val _failText = MutableLiveData<String>(null)
    val failText: LiveData<String> = _failText

    val personalData = userRepository.personalDataPreferencesFlow.asLiveData()

    fun createFamily() = viewModelScope.launch {
        if (userRepository.personalDataPreferencesFlow.first().familyId == null) {
            _status.postValue(HappinessApiStatus.LOADING)
            when (val response = userRepository.createFamily()) {
                is SafeResource.Success -> {
                    _status.postValue(HappinessApiStatus.DONE)
                }
                is SafeResource.Failure -> {
                    _status.postValue(HappinessApiStatus.ERROR)
                    if (response.throwable is HttpException) {
                        when (response.throwable.code()) {
                            401 -> {
                                _failText.postValue("Unauthorized to create family")
                            }
                            else -> {
                                _failText.postValue("Server failed. Please try again.")
                            }
                        }
                    } else {
                        _failText.postValue("Server failed. Please try again.")
                    }
                }
            }
        }
    }
}
