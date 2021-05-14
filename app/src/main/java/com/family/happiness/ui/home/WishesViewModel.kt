package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository

class WishesViewModel(private val repository: HappinessRepository): ViewModel() {
    val wishes = repository.wishes.asLiveData()
}