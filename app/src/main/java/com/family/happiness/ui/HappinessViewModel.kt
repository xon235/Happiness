package com.family.happiness.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.room.*
import kotlinx.coroutines.launch
import timber.log.Timber


class HappinessViewModel(
        private val repository: HappinessRepository,
        ): ViewModel() {

    val user: LiveData<User> = repository.user.asLiveData()

    fun silentSignIn(){
        user.value?.let {
            viewModelScope.launch {
                try {
                    repository.signIn(it.toAuthData())
                } catch (e: Exception){
                    Timber.d(e)
                }
            }
        }
    }

    fun newUser(user: User) {
        viewModelScope.launch { repository.newUser(user) }
    }

    fun clearUserData() {
        viewModelScope.launch { repository.clearUserData() }
    }

    val members: LiveData<List<Member>> = repository.members.asLiveData()

    fun updateMembers(){
        return
        user.value?.let {
            viewModelScope.launch {
                try {
                    repository.updateMembers(it.toAuthData())
                } catch (e: Exception){
                    Timber.d(e)
                }
            }
        }
    }

    fun joinFamily(phone: String, context: Context){
        user.value?.let {
            viewModelScope.launch {
                try {
                    repository.joinFamily(it.toAuthData(), phone)
                    Toast.makeText(context, "Joined Family", Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Timber.d(e)
                    Toast.makeText(context, "Join Family Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun leaveFamily(context: Context){
        user.value?.let {
            viewModelScope.launch {
                try {
                    repository.leaveFamily(it.toAuthData())
                    Toast.makeText(context, "Left Family", Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Timber.d(e)
                    Toast.makeText(context, "Leave Family Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    init {
        if(false){
            newUser(User(
                    "이수호",
                    "session",
                    "이수호",
                    "010-6634-3160",
                    "https://lh3.googleusercontent.com/a-/AOh14GhUQ75dRpaDl-pjyy4W7Sbi8FXZwQc47Y4KXs5MbQ=s96-c",
                    "abcdefghijk"
            ))
        }

        if(true){
            viewModelScope.launch {
                repository.insertImages(
                        listOf(
                                Image("url1", "id_user", "album1", "timestamp"),
                                Image("url2", "id_user", "album1", "timestamp"),
                                Image("url3", "id_user", "album2", "timestamp"),
                                Image("url4", "id_user", "album3", "timestamp"),
                                Image("url5", "id_user", "album3", "timestamp"),
                                Image("url6", "id_user", "album4", "timestamp")
                        )
                )
            }
        }

        if(true){
            viewModelScope.launch {
                repository.insertMembers(
                        listOf(
                                Member("id1", "name", "phone", "timestamp"),
                                Member("id2", "name", "phone", "timestamp"),
                                Member("id3", "name", "phone", "timestamp"),
                                Member("id4", "name", "phone", "timestamp"),
                        )
                )
            }
        }

        if(true){
            viewModelScope.launch {
                repository.insertWishes(
                    listOf(
                        Wish("id1", "user1", "title1", "content1", "timestamp_create1", null),
                        Wish("id2", "user1", "title2", "content2", "timestamp_create2", null),
                        Wish("id3", "user2", "title3", "content3", "timestamp_create3", "timestamp_finish1"),
                        Wish("id4", "user3", "title4", "content4", "timestamp_create4", "timestamp_finish1"),

                    )
                )
            }
        }
    }
}