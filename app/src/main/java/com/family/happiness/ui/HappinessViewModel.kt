package com.family.happiness.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.*
import com.family.happiness.room.user.User
import com.family.happiness.room.wish.Wish
import kotlinx.coroutines.launch
import timber.log.Timber


class HappinessViewModel(
    private val repository: HappinessRepository,
        ): ViewModel() {

//    val user: LiveData<User> = repository.user.asLiveData()
//
//    fun silentSignIn(){
//        user.value?.let {
//            viewModelScope.launch {
//                try {
//                    repository.signIn(it.toAuthData())
//                } catch (e: Exception){
//                    Timber.d(e)
//                }
//            }
//        }
//    }
//
//    fun newUser(user: User) {
//        viewModelScope.launch { repository.newUser(user) }
//    }
//
//    fun clearUserData() {
//        viewModelScope.launch { repository.clearUserData() }
//    }
//
//    val members: LiveData<List<Member>> = repository.members.asLiveData()
//
//    fun updateMembers(){
//        return
//        user.value?.let {
//            viewModelScope.launch {
//                try {
//                    repository.updateMembers(it.toAuthData())
//                } catch (e: Exception){
//                    Timber.d(e)
//                }
//            }
//        }
//    }
}