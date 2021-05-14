package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.room.Image

class AlbumViewModel(private val repository: HappinessRepository): ViewModel() {

    val images = repository.images.asLiveData()
    val albums = repository.albums.asLiveData()

    val viewAlbumInImages = MutableLiveData<Boolean>(true)

    private val _status = MutableLiveData<HappinessApiStatus>()
    val status: LiveData<HappinessApiStatus> = _status

    private val _navigateToSelectedImage = MutableLiveData<Image>()
    val navigateToSelectedProperty: LiveData<Image> = _navigateToSelectedImage

    val showAlbumItems = MutableLiveData<String>(null)

    fun displayPropertyDetails(image: Image) {
        _navigateToSelectedImage.value = image
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedImage.value = null
    }
}