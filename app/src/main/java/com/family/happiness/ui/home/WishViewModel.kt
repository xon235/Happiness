package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.repository.WishRepository

class WishViewModel(private val wishRepository: WishRepository): ViewModel() {
    val wishes = wishRepository.wishDetails.asLiveData()
}