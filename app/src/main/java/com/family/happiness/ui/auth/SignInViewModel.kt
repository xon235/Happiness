package com.family.happiness.ui.auth

import androidx.lifecycle.*
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.OAuthData
import com.family.happiness.network.response.PersonalDataResponse
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignInViewModel(private val userRepository: UserRepository): ViewModel() {

    val personalDataPreferences = userRepository.personalDataPreferencesFlow.asLiveData()

    private val _signInEnabled = MutableLiveData<Boolean>(true)
    val signInEnabled: LiveData<Boolean> = _signInEnabled

    private val _textViewVisible = MutableLiveData<Boolean>(false)
    val textViewVisible: LiveData<Boolean> = _textViewVisible

    private val _oAuthData = MutableLiveData<OAuthData>(null)
    val oAuthData: LiveData<OAuthData> = _oAuthData

    fun signIn(oAuthData: OAuthData) = viewModelScope.launch {
        when(val resource = userRepository.signIn(oAuthData)){
            is SafeResource.Success -> {
                userRepository.insertPersonalData(resource.value)
            }
            is SafeResource.Failure -> {
                if(resource.throwable is HttpException){
                    _oAuthData.postValue(oAuthData)
                }
                // TODO delete after test
                _oAuthData.postValue(oAuthData)

                setFailUi()
            }
        }
    }

    fun disableSignIn(){
        _signInEnabled.value = false
    }

    fun setFailUi() {
        _signInEnabled.postValue(true)
        _textViewVisible.postValue(true)
    }

    fun setToDefault(){
        _signInEnabled.postValue(true)
        _textViewVisible.postValue(false)
        _oAuthData.postValue(null)
    }

}
