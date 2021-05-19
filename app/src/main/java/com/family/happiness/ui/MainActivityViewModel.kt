package com.family.happiness.ui

import androidx.lifecycle.*
import com.family.happiness.Event
import com.family.happiness.network.SafeResource
import com.family.happiness.network.response.JoinFamilyResponse
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val personalData = userRepository.personalDataPreferencesFlow.asLiveData()
    val users = userRepository.users.asLiveData()
    val events = userRepository

    val me = userRepository.me.asLiveData()

    val members = userRepository.members.asLiveData()

    private val _joinFamilyEvent = MutableLiveData<Event<SafeResource<JoinFamilyResponse>>>()
    val joinFamilyEvent: LiveData<Event<SafeResource<JoinFamilyResponse>>> = _joinFamilyEvent

    fun joinFamily(familyId: String) = viewModelScope.launch {
        _joinFamilyEvent.value = Event(userRepository.joinFamily(familyId))
    }

    private val _leaveFamilyEvent = MutableLiveData<Event<SafeResource<Unit>>>()
    val leaveFamilyEvent: LiveData<Event<SafeResource<Unit>>> = _leaveFamilyEvent

    fun leaveFamily() = viewModelScope.launch {
        _leaveFamilyEvent.value = Event(userRepository.leaveFamily())
    }

    fun clearUserData() = viewModelScope.launch {
        userRepository.deleteAllPersonalData()
    }

    fun updateMembers(){}

}