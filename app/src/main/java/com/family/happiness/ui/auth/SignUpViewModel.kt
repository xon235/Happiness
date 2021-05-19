package com.family.happiness.ui.auth

import androidx.lifecycle.*
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _inputEnabled = MutableLiveData<Boolean>(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _failText = MutableLiveData<String>(null)
    val failText: LiveData<String> = _failText

    private val _navigateToSmsVerification = MutableLiveData<Boolean>(false)
    val navigateToSmsVerification: LiveData<Boolean> = _navigateToSmsVerification

    fun getSmsCode(phone: String) = viewModelScope.launch {
        _inputEnabled.postValue(false)
        when(val response = userRepository.getSmsCode(phone)){
            is SafeResource.Success -> {_navigateToSmsVerification.postValue(true)}
            is SafeResource.Failure -> {
                if(response.throwable is HttpException){
                    _failText.postValue("Invalid phone number.")

                    // TODO delete after test
                    _navigateToSmsVerification.postValue(true)
                } else {
                    _failText.postValue("Server failed. Please try again.")
                }
                _inputEnabled.postValue(true)
            }
        }
    }

//    var phone = ""

//    fun requestSmsCode() {
//        _buttonEnabled.value = false
//        viewModelScope.launch {
////            withContext(Dispatchers.IO){
////                try {
////                    val response = HappinessApiService.retrofitService.requestSmsCode(PhoneData(phone))
////                    if(response.result) {
////                        _navigateToSmsVerification.postValue(true)
////                    } else {
////                        throw HappinessApiException("Sever response: false")
////                    }
////                } catch (e: Exception){
////                    Timber.d(e)
////                    setFailUi()
////                }
////            }
//        }
//    }

    fun setToDefault(){
        _inputEnabled.postValue(true)
        _failText.postValue(null)
        _navigateToSmsVerification.postValue(false)
    }
}
