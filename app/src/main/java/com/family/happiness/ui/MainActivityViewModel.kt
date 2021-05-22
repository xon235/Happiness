package com.family.happiness.ui

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.network.response.JoinFamilyResponse
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

    private val _joinFamilyEvent = MutableLiveData<Flag<SafeResource<JoinFamilyResponse>>>()
    val joinFamilyFlag: LiveData<Flag<SafeResource<JoinFamilyResponse>>> = _joinFamilyEvent

    fun joinFamily(familyId: String) = viewModelScope.launch {
        _joinFamilyEvent.value = Flag(userRepository.joinFamily(familyId))
    }

    private val _leaveFamilyEvent = MutableLiveData<Flag<SafeResource<Unit>>>()
    val leaveFamilyFlag: LiveData<Flag<SafeResource<Unit>>> = _leaveFamilyEvent

    fun leaveFamily() = viewModelScope.launch {
        _leaveFamilyEvent.value = Flag(userRepository.leaveFamily())
    }

    fun clearUserData() = viewModelScope.launch {
        userRepository.deleteAllPersonalData()
    }

    fun updateMembers(){}

}