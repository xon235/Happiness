package com.family.happiness.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.family.happiness.HappinessRepository

class ViewModelFactory(private val repository: HappinessRepository)
    : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T{
        return when {
            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> AlbumViewModel(repository) as T
            modelClass.isAssignableFrom(CreateFamilyViewModel::class.java) -> CreateFamilyViewModel(repository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(repository) as T
            modelClass.isAssignableFrom(HappinessViewModel::class.java) -> HappinessViewModel(repository) as T
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(repository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(repository) as T
            modelClass.isAssignableFrom(UploadImageViewModel::class.java) -> UploadImageViewModel(repository) as T
            modelClass.isAssignableFrom(WishesViewModel::class.java) -> WishesViewModel(repository) as T
            modelClass.isAssignableFrom(WishSelectContributorViewModel::class.java) -> WishSelectContributorViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}