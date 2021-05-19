package com.family.happiness.ui.photoupload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.event.Event

class UploadImageViewModel(private val repository: HappinessRepository) : ViewModel() {
//    fun upload(isNewAlbum: Boolean, album: String, tagged: List<Member>, files: List<MultipartBody.Part>) {
//        viewModelScope.launch {
//            try {
//                repository.uploadImage(isNewAlbum, album, tagged, files)
//            } catch (e: Exception){
//                Timber.d(e)
//            }
//        }
//    }

    val events: LiveData<List<Event>> = MutableLiveData(emptyList<Event>())
//    val events = repository.albums.asLiveData()
}