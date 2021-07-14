package com.family.happiness.ui.photodetail

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PhotoDetailViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    fun setPhoto(photo: Photo) {
        _photo.value = photo
    }

    val photoDetail = Transformations.switchMap(_photo) {
        albumRepository.getPhotoDetailByUrl(it.url).asLiveData()
    }

    val events = Transformations.switchMap(photoDetail) { photoDetail ->
        albumRepository.events.map { eventsList ->
            eventsList.filter { it != photoDetail.event }
        }.asLiveData()
    }

    private val _inputEnabled = MutableLiveData(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _eventChangedFlag = MutableLiveData<Flag<Boolean>>()
    val eventChangedFlag: LiveData<Flag<Boolean>> = _eventChangedFlag

    fun changeEvent(photo: Photo, event: Event) = viewModelScope.launch {
        _inputEnabled.value = false
        when (val resource = albumRepository.movePhoto(photo, event)) {
            is SafeResource.Success -> {
                _eventChangedFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _eventChangedFlag.value = Flag(false)
            }
        }
        _inputEnabled.value = true
    }

    private val _deletePhotoFlag = MutableLiveData<Flag<Boolean>>()
    val deletePhotoFlag: LiveData<Flag<Boolean>> = _deletePhotoFlag

    fun deletePhoto(photo: Photo) = viewModelScope.launch {
        _inputEnabled.value = false
        when (val resource = albumRepository.deletePhoto(photo)) {
            is SafeResource.Success -> {
                _deletePhotoFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _deletePhotoFlag.value = Flag(false)
            }
        }
        _inputEnabled.value = true
    }
}