package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo

class AlbumViewModel(private val albumRepository: AlbumRepository): ViewModel() {

    val events = albumRepository.events.asLiveData()
    val selectedEvent: MutableLiveData<Event> = MutableLiveData(null)
    val photos = Transformations.switchMap(selectedEvent){
        if(it == null){
            albumRepository.photos.asLiveData()
        } else {
            albumRepository.getPhotosByEvent(it).asLiveData()
        }
    }

    private val _navigateToSelectedImage = MutableLiveData<Flag<Photo>>()
    val navigateToSelectedProperty: LiveData<Flag<Photo>> = _navigateToSelectedImage

    val isEventView = MutableLiveData(false)

    fun displayPropertyDetails(photo: Photo) {
        _navigateToSelectedImage.value = Flag(photo)
    }
}