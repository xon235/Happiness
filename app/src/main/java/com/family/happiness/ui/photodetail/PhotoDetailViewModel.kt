package com.family.happiness.ui.photodetail

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDetail
import kotlinx.coroutines.launch

class PhotoDetailViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    fun setPhoto(photo: Photo) { _photo.value = photo }

    val photoDetail = Transformations.switchMap(_photo){
        albumRepository.getPhotoDetailByUrl(it.url).asLiveData()
    }

    val events = albumRepository.events.asLiveData()

    private val _eventChangedFlag = MutableLiveData<Flag<Boolean>>()
    val eventChangedFlag: LiveData<Flag<Boolean>>  = _eventChangedFlag

    fun changeEvent(photo: Photo, event: Event) = viewModelScope.launch {
        when(val resource = albumRepository.movePhoto(photo, event)){
            is SafeResource.Success -> {
                _eventChangedFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _eventChangedFlag.value = Flag(false)
            }
        }
    }

    fun deletePhoto() = viewModelScope.launch {
        _photo.value?.let {
            albumRepository.deletePhoto(it)
        }
    }
}