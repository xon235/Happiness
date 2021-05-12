package com.family.happiness.viewmodels

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.network.HappinessApiStatus
import com.family.happiness.rooms.Image
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: HappinessRepository): ViewModel() {

    val image = MutableLiveData<Image>(null)
    val albums = repository.albums.asLiveData()

    fun deleteImage(image: Image){
        viewModelScope.launch {
            repository.deleteImage(image)
        }
    }

    fun moveImage(image: Image, album: String) {
        viewModelScope.launch {
            repository.moveImage(image, album)
        }
    }
}