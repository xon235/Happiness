package com.family.happiness.ui.photodetail

import androidx.lifecycle.*
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.launch

class DetailViewModel(private val albumRepository: AlbumRepository): ViewModel() {

    val photo = MutableLiveData<Photo>(null)
    val events = albumRepository.events

    fun deleteImage(photo: Photo) = viewModelScope.launch {

    }

    fun moveImage(photo: Photo, event: Event) = viewModelScope.launch {

    }
}