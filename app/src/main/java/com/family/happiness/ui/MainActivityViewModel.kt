package com.family.happiness.ui

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val personalData = userRepository.personalDataPreferencesFlow.asLiveData()
    val users = userRepository.users.asLiveData()
    val events = userRepository

    val me = userRepository.me.asLiveData()

    val members = userRepository.members.asLiveData()

    private val _joinFamilyFlag = MutableLiveData<Flag<Boolean>>()
    val joinFamilyFlag: LiveData<Flag<Boolean>> = _joinFamilyFlag

    fun joinFamily(familyId: String) = viewModelScope.launch {
        when(val resource = userRepository.joinFamily(familyId)){
            is SafeResource.Success -> {
                _joinFamilyFlag.value = Flag(true)
                userRepository.insertFamilyId(resource.value.familyId)
            }
            is SafeResource.Failure -> {
                _joinFamilyFlag.value = Flag(false)
            }
        }
    }

    private val _leaveFamilyEvent = MutableLiveData<Flag<Boolean>>()
    val leaveFamilyFlag: LiveData<Flag<Boolean>> = _leaveFamilyEvent

    fun leaveFamily() = viewModelScope.launch {
        when(userRepository.leaveFamily()){
            is SafeResource.Success -> {
                _leaveFamilyEvent.value = Flag(true)
                userRepository.deleteFamilyId()
            }
            is SafeResource.Failure -> {
                _leaveFamilyEvent.value = Flag(false)
            }
        }
    }

    fun syncUser() = viewModelScope.launch {
        when(val resource = userRepository.syncUser()){
            is SafeResource.Success -> { }
            is SafeResource.Failure -> { }
        }
    }

    fun clearUserData() = viewModelScope.launch {
        userRepository.deleteAllPersonalData()
    }
}