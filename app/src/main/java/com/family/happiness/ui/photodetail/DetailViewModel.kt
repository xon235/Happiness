package com.family.happiness.ui.photodetail

import androidx.lifecycle.*
import com.family.happiness.HappinessRepository
import com.family.happiness.room.Image
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