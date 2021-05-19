package com.family.happiness.ui.createfamily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.user.User
import kotlinx.coroutines.launch

class CreateFamilyViewModel(private val repository: HappinessRepository): ViewModel() {
    private val _status = MutableLiveData<HappinessApiStatus>(null)
    val status: LiveData<HappinessApiStatus> = _status

    private val _message = MutableLiveData<String>(null)
    val message: LiveData<String> = _message

    val user: LiveData<User> = MutableLiveData<User>()

    fun createFamily() {
        viewModelScope.launch {
//            _status.value = HappinessApiStatus.LOADING
//            try {
//                repository.createFamily(user.value!!.toAuthData())
//                _status.value = HappinessApiStatus.DONE
//            } catch (e: HappinessApiException){
//                _status.value = HappinessApiStatus.ERROR
//                _message.value = e.message
//            } catch (e: Exception) {
//                _status.value = HappinessApiStatus.ERROR
//                _message.value = "Server Failed"
//                Timber.d(e)
//            }
        }
    }
}
