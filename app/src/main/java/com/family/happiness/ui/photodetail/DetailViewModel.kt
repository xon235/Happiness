package com.family.happiness.ui.photodetail

import androidx.lifecycle.*
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: HappinessRepository): ViewModel() {

    val photo = MutableLiveData<Photo>(null)
    val events = MutableLiveData<List<Event>>(null)

    fun deleteImage(image: Photo){
        viewModelScope.launch {
//            repository.deleteImage(image)
        }
    }

    fun moveImage(image: Photo, album: String) {
        viewModelScope.launch {
//            repository.moveImage(image, album)
        }
    }
}