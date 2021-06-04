package com.family.happiness.ui.auth

import androidx.lifecycle.*
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.OAuthData
import com.family.happiness.network.request.SignInData
import com.family.happiness.network.response.PersonalDataResponse
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val personalData = userRepository.personalDataPreferencesFlow.asLiveData()

    private val _inputEnabled = MutableLiveData<Boolean>(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _failText = MutableLiveData<String>()
    val failText: LiveData<String> = _failText

    private val _oAuthData = MutableLiveData<OAuthData>(null)
    val oAuthData: LiveData<OAuthData> = _oAuthData

    fun signIn(oAuthData: OAuthData) = viewModelScope.launch {
        when (val resource = userRepository.signIn(oAuthData)) {
            is SafeResource.Success -> { }
            is SafeResource.Failure -> {
                if (resource.throwable is HttpException){
                    when(resource.throwable.code()){
                        401 -> _oAuthData.postValue(oAuthData)
                        else -> setFailUi("Failed to sign in with Google")
                    }
                } else {
                    setFailUi("Failed to sign in with Google")
                }
            }
        }
    }

    fun disableInput() {
        _inputEnabled.value = false
    }

    fun setFailUi(text: String) {
        _inputEnabled.postValue(true)
        _failText.postValue(text)
    }

    fun setToDefault() {
        _inputEnabled.postValue(true)
        _failText.postValue(null)
        _oAuthData.postValue(null)
    }

}
