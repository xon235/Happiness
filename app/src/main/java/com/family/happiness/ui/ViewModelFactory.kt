package com.family.happiness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.family.happiness.HappinessRepository
import com.family.happiness.ui.home.AlbumViewModel
import com.family.happiness.ui.createfamily.CreateFamilyViewModel
import com.family.happiness.ui.auth.SignInViewModel
import com.family.happiness.ui.auth.SignUpViewModel
import com.family.happiness.ui.home.WishesViewModel
import com.family.happiness.ui.photodetail.DetailViewModel
import com.family.happiness.ui.photoupload.UploadImageViewModel
import com.family.happiness.viewmodel.WishSelectContributorViewModel

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