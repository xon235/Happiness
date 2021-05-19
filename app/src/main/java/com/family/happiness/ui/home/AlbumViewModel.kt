package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.repository.HappinessRepository
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo

class AlbumViewModel(private val repository: HappinessRepository): ViewModel() {

    val photos: LiveData<List<Photo>> = MutableLiveData()
    val events: LiveData<List<Event>> = MutableLiveData()

    val viewAlbumInPhotos = MutableLiveData(true)

    private val _status = MutableLiveData<HappinessApiStatus>()
    val status: LiveData<HappinessApiStatus> = _status

    private val _navigateToSelectedImage = MutableLiveData<Photo>()
    val navigateToSelectedProperty: LiveData<Photo> = _navigateToSelectedImage

    val showAlbumItems = MutableLiveData<String>(null)

    fun displayPropertyDetails(photo: Photo) {
        _navigateToSelectedImage.value = photo
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedImage.value = null
    }

//    val images = repository.images.asLiveData()
//    val albums = repository.albums.asLiveData()
//
//    val viewAlbumInImages = MutableLiveData<Boolean>(true)
//
//    private val _status = MutableLiveData<HappinessApiStatus>()
//    val status: LiveData<HappinessApiStatus> = _status
//
//    private val _navigateToSelectedImage = MutableLiveData<Image>()
//    val navigateToSelectedProperty: LiveData<Image> = _navigateToSelectedImage
//
//    val showAlbumItems = MutableLiveData<String>(null)
//
//    fun displayPropertyDetails(image: Image) {
//        _navigateToSelectedImage.value = image
//    }
//
//    fun displayPropertyDetailsComplete() {
//        _navigateToSelectedImage.value = null
//    }
}