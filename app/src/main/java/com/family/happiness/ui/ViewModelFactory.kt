package com.family.happiness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.family.happiness.HappinessApplication
import com.family.happiness.ui.home.AlbumViewModel
import com.family.happiness.ui.createfamily.CreateFamilyViewModel
import com.family.happiness.ui.auth.SignInViewModel
import com.family.happiness.ui.auth.SignUpViewModel
import com.family.happiness.ui.home.MailViewModel
import com.family.happiness.ui.home.WishesViewModel
import com.family.happiness.ui.photodetail.DetailViewModel
import com.family.happiness.ui.photoupload.UploadImageViewModel
import com.family.happiness.viewmodel.WishFinisViewModel

class ViewModelFactory(private val app: HappinessApplication)
    : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T{
        return when {
//            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> AlbumViewModel(app.happinessRepository) as T
//            modelClass.isAssignableFrom(CreateFamilyViewModel::class.java) -> CreateFamilyViewModel(app.happinessRepository) as T
//            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(app.happinessRepository) as T
//            modelClass.isAssignableFrom(HappinessViewModel::class.java) -> HappinessViewModel(app.happinessRepository) as T
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(app.happinessRepository) as T
//            modelClass.isAssignableFrom(UploadImageViewModel::class.java) -> UploadImageViewModel(app.happinessRepository) as T
//            modelClass.isAssignableFrom(WishesViewModel::class.java) -> WishesViewModel(app.happinessRepository) as T
//            modelClass.isAssignableFrom(WishFinisViewModel::class.java) -> WishFinisViewModel(app.happinessRepository) as T
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(MailViewModel::class.java) -> MailViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class ${modelClass.canonicalName}")
        }
    }
}