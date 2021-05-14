package com.family.happiness.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.R
import com.family.happiness.network.HappinessApiException
import com.family.happiness.network.TokenData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.lang.Exception

class SignInViewModel(private val repository: HappinessRepository): ViewModel() {
    private val _buttonEnabled = MutableLiveData<Boolean>(true)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    private val _textViewVisible = MutableLiveData<Boolean>(false)
    val textViewVisible: LiveData<Boolean> = _textViewVisible

    private val _accountInfo = MutableLiveData<AccountInfo>(null)
    val accountInfo: LiveData<AccountInfo> = _accountInfo

    fun getSignInIntent(activity: Activity): Intent {
        _buttonEnabled.value = false
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.server_client_id))
                .build()
        return GoogleSignIn.getClient(activity, gso).signInIntent
    }

    fun signInWithToken(accountInfo: AccountInfo) {
        viewModelScope.launch {
            try {
                repository.signInWithToken(accountInfo.toTokenData())
            } catch (e: HappinessApiException){
                Timber.d("Failed to sign in with token")
                Timber.d(e)
                _accountInfo.postValue(accountInfo)
            }
            catch (e: Exception){
                Timber.d(e)
            }
        }
    }

    fun setFailUi() {
        _buttonEnabled.postValue(true)
        _textViewVisible.postValue(true)
    }

    fun setToDefault(activity: Activity){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
        GoogleSignIn.getClient(activity, gso).signOut()
        _buttonEnabled.postValue(true)
        _textViewVisible.postValue(true)
        _accountInfo.postValue(null)
    }

}

@Parcelize
data class AccountInfo(
        val type: String,
        val token: String,
        val name: String,
        val photoUrl: String
) : Parcelable

fun AccountInfo.toTokenData(): TokenData{
    return TokenData(
            type,
            token
    )
}
