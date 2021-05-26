package com.family.happiness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.family.happiness.HappinessApplication
import com.family.happiness.room.wish.WishDetail
import com.family.happiness.ui.home.AlbumViewModel
import com.family.happiness.ui.createfamily.CreateFamilyViewModel
import com.family.happiness.ui.auth.SignInViewModel
import com.family.happiness.ui.auth.SignUpViewModel
import com.family.happiness.ui.auth.SmsVerificationViewModel
import com.family.happiness.ui.home.MailViewModel
import com.family.happiness.ui.home.WishViewModel
import com.family.happiness.ui.mailread.MailReadViewModel
import com.family.happiness.ui.mailwrite.MailWriteViewModel
import com.family.happiness.ui.photodetail.PhotoDetailViewModel
import com.family.happiness.ui.photoupload.PhotoUploadViewModel
import com.family.happiness.ui.splash.SplashViewModel
import com.family.happiness.ui.wishdetail.WishDetailViewModel
import com.family.happiness.ui.wishfinish.WishFinisViewModel
import com.family.happiness.ui.wishnwrite.WishWriteViewModel

class ViewModelFactory(private val app: HappinessApplication)
    : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T{
        return when {
            // splash
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(app.userRepository) as T

            // auth
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(SmsVerificationViewModel::class.java) -> SmsVerificationViewModel(app.userRepository) as T

            // home
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(app.userRepository) as T
            modelClass.isAssignableFrom(CreateFamilyViewModel::class.java) -> CreateFamilyViewModel(app.userRepository) as T

            // mail
            modelClass.isAssignableFrom(MailViewModel::class.java) -> MailViewModel(app.mailRepository) as T
            modelClass.isAssignableFrom(MailWriteViewModel::class.java) -> MailWriteViewModel(app.userRepository, app.mailRepository) as T
            modelClass.isAssignableFrom(MailReadViewModel::class.java) -> MailReadViewModel(app.userRepository, app.mailRepository) as T

            // album
            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> AlbumViewModel(app.albumRepository) as T
            modelClass.isAssignableFrom(PhotoDetailViewModel::class.java) -> PhotoDetailViewModel(app.albumRepository) as T
            modelClass.isAssignableFrom(PhotoUploadViewModel::class.java) -> PhotoUploadViewModel(app.userRepository, app.albumRepository) as T

            // wish
            modelClass.isAssignableFrom(WishViewModel::class.java) -> WishViewModel(app.wishRepository) as T
            modelClass.isAssignableFrom(WishDetailViewModel::class.java) -> WishDetailViewModel() as T
            modelClass.isAssignableFrom(WishWriteViewModel::class.java) -> WishWriteViewModel(app.wishRepository) as T
            modelClass.isAssignableFrom(WishFinisViewModel::class.java) -> WishFinisViewModel(app.userRepository, app.wishRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class ${modelClass.canonicalName}")
        }
    }
}