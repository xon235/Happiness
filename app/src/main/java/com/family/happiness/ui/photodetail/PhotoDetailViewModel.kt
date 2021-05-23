package com.family.happiness.ui.photodetail

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.launch

class PhotoDetailViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    val events = albumRepository.events.asLiveData()

    fun setPhoto(photo: Photo) {
        _photo.value = photo
    }

    val photo = Transformations.switchMap(_photo){
        albumRepository.getPhotoByUrl(it.url).asLiveData()
    }
    val event = Transformations.switchMap(photo){
        albumRepository.getEventByPhoto(it).asLiveData()
    }

    private val _eventChangedFlag = MutableLiveData<Flag<Event>>()
    val eventChangedFlag: LiveData<Flag<Event>>  = _eventChangedFlag

    fun changeEvent(event: Event) = viewModelScope.launch {
        _photo.value?.let {
            albumRepository.changePhotoEvent(it, event)
            _eventChangedFlag.value = Flag(event)
        }
    }

    fun deletePhoto() = viewModelScope.launch {
        _photo.value?.let {
            albumRepository.deletePhoto(it)
        }
    }
}