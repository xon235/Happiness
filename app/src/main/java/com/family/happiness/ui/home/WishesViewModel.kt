package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.wish.Wish

class WishesViewModel(private val repository: HappinessRepository): ViewModel() {
//    val wishes = repository.wishes.asLiveData()
    val wishes = MutableLiveData(emptyList<Wish>())
}