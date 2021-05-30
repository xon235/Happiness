package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.launch

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

    val isEventView = MutableLiveData(false)

    private val _navigateToSelectedImage = MutableLiveData<Flag<Photo>>()
    val navigateToSelectedProperty: LiveData<Flag<Photo>> = _navigateToSelectedImage

    fun displayPropertyDetails(photo: Photo) {
        _navigateToSelectedImage.value = Flag(photo)
    }

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _syncFinishFlag = MutableLiveData<Flag<Boolean>>()
    val syncFinishFlag: LiveData<Flag<Boolean>> = _syncFinishFlag

    fun syncAlbum() = viewModelScope.launch {
        _isRefreshing.value = true
        when(val resource = albumRepository.syncPhoto()){
            is SafeResource.Success -> {
//                resource.value.events?.let { albumRepository.syncEvent(it) }
//                resource.value.photos?.let { albumRepository.syncPhoto(it) }
                _syncFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _syncFinishFlag.value = Flag(false)
            }
        }
        _isRefreshing.value = false
    }
}