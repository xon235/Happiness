package com.family.happiness.viewmodels

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.network.HappinessApi
import com.family.happiness.network.HappinessApiException
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.network.PhoneData
import com.family.happiness.rooms.User
import com.family.happiness.rooms.toAuthData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class CreateFamilyViewModel(private val repository: HappinessRepository): ViewModel() {
    private val _status = MutableLiveData<HappinessApiStatus>(null)
    val status: LiveData<HappinessApiStatus> = _status

    private val _message = MutableLiveData<String>(null)
    val message: LiveData<String> = _message

    val user = repository.user.asLiveData()

    fun createFamily() {
        viewModelScope.launch {
            _status.value = HappinessApiStatus.LOADING
            try {
                repository.createFamily(user.value!!.toAuthData())
                _status.value = HappinessApiStatus.DONE
            } catch (e: HappinessApiException){
                _status.value = HappinessApiStatus.ERROR
                _message.value = e.message
            } catch (e: Exception) {
                _status.value = HappinessApiStatus.ERROR
                _message.value = "Server Failed"
                Timber.d(e)
            }
        }
    }
}
