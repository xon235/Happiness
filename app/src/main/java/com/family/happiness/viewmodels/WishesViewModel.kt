package com.family.happiness.viewmodels

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.rooms.Image

class WishesViewModel(private val repository: HappinessRepository): ViewModel() {
    val wishes = repository.wishes.asLiveData()
}