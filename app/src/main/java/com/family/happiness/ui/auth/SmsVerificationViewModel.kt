package com.family.happiness.ui.auth

import androidx.lifecycle.*
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.SignUpData
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SmsVerificationViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _inputEnabled = MutableLiveData(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _failText = MutableLiveData<String>()
    val failText: LiveData<String> = _failText

    private val _navigateToSignIn = MutableLiveData(false)
    val navigateToSignIn: LiveData<Boolean> = _navigateToSignIn

    fun signUp(signUpData: SignUpData) = viewModelScope.launch {
        _inputEnabled.postValue(false)
        when(val resource = userRepository.signUp(signUpData)){
            is SafeResource.Success -> {
                _navigateToSignIn.postValue(true)
            }
            is SafeResource.Failure -> {
                _inputEnabled.postValue(true)
                _failText.postValue(
                    if(resource.throwable is HttpException){
                        when(resource.throwable.code()){
                            400 -> "Incorrect code. Please check again."
                            else -> "Server failed. Please try again."
                        }
                    } else {
                        "Server failed. Please try again."
                    }
                )
            }
        }
    }

    fun setToDefault(){
        _inputEnabled.postValue(true)
        _failText.postValue(null)
    }
}