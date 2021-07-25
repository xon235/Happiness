package com.family.happiness.ui

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.repository.MailRepository
import com.family.happiness.repository.UserRepository
import com.family.happiness.repository.WishRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


class MainActivityViewModel(
    private val userRepository: UserRepository,
    private val mailRepository: MailRepository,
    private val albumRepository: AlbumRepository,
    private val wishRepository: WishRepository
) : ViewModel() {

    val personalData = userRepository.personalDataPreferencesFlow.asLiveData()
    val users = userRepository.users.asLiveData()
    val events = userRepository

    val me = userRepository.me.asLiveData()

    val members = userRepository.members.distinctUntilChanged().asLiveData()

    private val _joinFamilyFlag = MutableLiveData<Flag<Boolean>>()
    val joinFamilyFlag: LiveData<Flag<Boolean>> = _joinFamilyFlag

    fun joinFamily(familyId: String) = viewModelScope.launch {
        when(val resource = userRepository.joinFamily(familyId)){
            is SafeResource.Success -> {
                _joinFamilyFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _joinFamilyFlag.value = Flag(false)
            }
        }
    }

    private val _leaveFamilyFlag = MutableLiveData<Flag<Boolean>>()
    val leaveFamilyFlag: LiveData<Flag<Boolean>> = _leaveFamilyFlag

    fun leaveFamily() = viewModelScope.launch {
        when(userRepository.leaveFamily()){
            is SafeResource.Success -> {
                _leaveFamilyFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _leaveFamilyFlag.value = Flag(false)
            }
        }
    }

    private val _syncUserFlag = MutableLiveData<Flag<Boolean>>()
    val syncUserFlag: LiveData<Flag<Boolean>> = _syncUserFlag

    fun syncUser() = viewModelScope.launch {
        when(val resource = userRepository.syncUser()){
            is SafeResource.Success -> {
                _syncUserFlag.value = Flag(true)
                mailRepository.syncMail()
                albumRepository.syncPhoto()
                wishRepository.syncWish()
            }
            is SafeResource.Failure -> {
                _syncUserFlag.value = Flag(false)
            }
        }
    }

    fun clearUserData() = viewModelScope.launch {
        userRepository.deleteAllData()
        mailRepository.deleteAllData()
        albumRepository.deleteAllData()
        wishRepository.deleteAllData()
    }
}