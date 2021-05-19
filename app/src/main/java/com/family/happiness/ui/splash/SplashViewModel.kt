package com.family.happiness.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.family.happiness.repository.UserRepository

class SplashViewModel(private val userRepository: UserRepository): ViewModel() {
    val personalData = userRepository.personalDataPreferencesFlow.asLiveData()
}