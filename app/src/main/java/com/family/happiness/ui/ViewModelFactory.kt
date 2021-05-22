package com.family.happiness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.family.happiness.HappinessApplication
import com.family.happiness.ui.home.AlbumViewModel
import com.family.happiness.ui.createfamily.CreateFamilyViewModel
import com.family.happiness.ui.auth.SignInViewModel
import com.family.happiness.ui.auth.SignUpViewModel
import com.family.happiness.ui.auth.SmsVerificationViewModel
import com.family.happiness.ui.home.MailViewModel
import com.family.happiness.ui.home.WishesViewModel
import com.family.happiness.ui.photodetail.DetailViewModel
import com.family.happiness.ui.photoupload.UploadImageViewModel
import com.family.happiness.ui.splash.SplashViewModel
import com.family.happiness.viewmodel.WishFinisViewModel

class ViewModelFactory(private val app: HappinessApplication)
    : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T{
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(SmsVerificationViewModel::class.java) -> SmsVerificationViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(CreateFamilyViewModel::class.java) -> CreateFamilyViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(MailViewModel::class.java) -> MailViewModel() as T
            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> AlbumViewModel(app.albumRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(app.albumRepository) as T
            modelClass.isAssignableFrom(UploadImageViewModel::class.java) -> UploadImageViewModel(app.userRepository, app.albumRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class ${modelClass.canonicalName}")
        }
    }
}