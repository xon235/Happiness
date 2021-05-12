package com.family.happiness.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.family.happiness.HappinessRepository
import com.family.happiness.rooms.Member
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber

class UploadImageViewModel(private val repository: HappinessRepository): ViewModel() {
    fun upload(isNewAlbum: Boolean, album: String, tagged: List<Member>, files: List<MultipartBody.Part>) {
        viewModelScope.launch {
            try {
                repository.uploadImage(isNewAlbum, album, tagged, files)
            } catch (e: Exception){
                Timber.d(e)
            }
        }
    }

    val albums = repository.albums.asLiveData()
}