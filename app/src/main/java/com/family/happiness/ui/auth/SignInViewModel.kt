package com.family.happiness.ui.auth

import androidx.lifecycle.*
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.OAuthData
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
            is SafeResource.Success -> {
                userRepository.insertPersonalData(resource.value)
            }
            is SafeResource.Failure -> {
                if (resource.throwable is HttpException && resource.throwable.code() == 404) {
                    _oAuthData.postValue(oAuthData)
                } else {
                    // TODO delete after test
                    _oAuthData.postValue(oAuthData)

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
