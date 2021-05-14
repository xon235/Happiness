package com.family.happiness.ui.auth

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.network.HappinessApiService
import com.family.happiness.network.HappinessApiException
import com.family.happiness.network.PhoneData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class SignUpViewModel(private val repository: HappinessRepository): ViewModel() {
    private val _buttonEnabled = MutableLiveData<Boolean>(true)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    private val _textViewVisible = MutableLiveData<Boolean>(false)
    val textViewVisible: LiveData<Boolean> = _textViewVisible

    private val _navigateToSmsVerification = MutableLiveData<Boolean>(false)
    val navigateToSmsVerification: LiveData<Boolean> = _navigateToSmsVerification

    var phone = ""

    fun requestSmsCode() {
        _buttonEnabled.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = HappinessApiService.retrofitService.requestSmsCode(PhoneData(phone))
                    if(response.result) {
                        _navigateToSmsVerification.postValue(true)
                    } else {
                        throw HappinessApiException("Sever response: false")
                    }
                } catch (e: Exception){
                    Timber.d(e)
                    setFailUi()
                }
            }
        }
    }

    private fun setFailUi() {
        _buttonEnabled.postValue(true)
        _textViewVisible.postValue(true)
    }

    fun setToDefault(){
        _buttonEnabled.postValue(true)
        _textViewVisible.postValue(false)
        _navigateToSmsVerification.postValue(false)
    }
}
