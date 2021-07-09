package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.adapter.AlbumListAdapter
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.launch
import timber.log.Timber

enum class AlbumViewState {
    AllPhotos, Events, PhotosByEvent
}

class AlbumViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    val selectedEvent = MutableLiveData<Event>()

    private val _photosByEvent = Transformations.switchMap(selectedEvent) {
        if(it == null) {
            MutableLiveData(emptyList())
        } else {
            albumRepository.getPhotosByEvent(it).asLiveData()
        }
    }

    val albumViewState = MutableLiveData(AlbumViewState.AllPhotos)
    val albumItems: LiveData<List<AlbumListAdapter.AlbumItem>> = Transformations.switchMap(albumViewState) { albumViewState ->
        when(albumViewState) {
            AlbumViewState.AllPhotos -> albumRepository.photos.asLiveData().switchMap { allPhotos ->
                MutableLiveData(allPhotos.map { AlbumListAdapter.AlbumItem.PhotoItem(it) })
            }
            AlbumViewState.Events -> albumRepository.events.asLiveData().switchMap { events ->
                MutableLiveData(events.map { AlbumListAdapter.AlbumItem.EventItem(it) })
            }
            AlbumViewState.PhotosByEvent -> _photosByEvent.switchMap { photosByEvent ->
                MutableLiveData(photosByEvent.map { AlbumListAdapter.AlbumItem.PhotoItem(it) })
            }
        }
    }

    private val _navigateToSelectedImage = MutableLiveData<Flag<Photo>>()
    val navigateToSelectedImage: LiveData<Flag<Photo>> = _navigateToSelectedImage

    fun displayImageDetails(photo: Photo) {
        _navigateToSelectedImage.value = Flag(photo)
    }

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _syncFinishFlag = MutableLiveData<Flag<Boolean>>()
    val syncFinishFlag: LiveData<Flag<Boolean>> = _syncFinishFlag

    fun syncAlbum() = viewModelScope.launch {
        _isRefreshing.value = true
        when (val resource = albumRepository.syncPhoto()) {
            is SafeResource.Success -> {
                _syncFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _syncFinishFlag.value = Flag(false)
            }
        }
        _isRefreshing.value = false
    }
}