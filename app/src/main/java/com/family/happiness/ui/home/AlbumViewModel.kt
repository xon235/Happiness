package com.family.happiness.ui.home

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.adapter.AlbumListAdapter
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.room.event.Event
import com.family.happiness.room.photo.Photo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

enum class AlbumViewState {
    AllPhotos, Events, PhotosByEvent
}

class AlbumViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _selectedEvent = MutableLiveData<Event>()
    val selectedEvent: LiveData<Event> = _selectedEvent

    private val _albumViewState = MutableLiveData(AlbumViewState.AllPhotos)
    val albumViewState: LiveData<AlbumViewState> = _albumViewState

    fun setAllPhotosViewState() {
        _selectedEvent.postValue(null)
        _albumViewState.postValue(AlbumViewState.AllPhotos)
    }

    fun setEventsViewState() {
        _selectedEvent.postValue(null)
        _albumViewState.postValue(AlbumViewState.Events)
    }

    fun setPhotosByEventViewState(event: Event) {
        _selectedEvent.postValue(event)
        _albumViewState.postValue(AlbumViewState.PhotosByEvent)
    }

    val albumItems: LiveData<List<AlbumListAdapter.AlbumItem>> =
        Transformations.switchMap(albumViewState) { albumViewState ->
            when (albumViewState) {
                AlbumViewState.AllPhotos -> albumRepository.photos.map { allPhotos ->
                    allPhotos.map { AlbumListAdapter.AlbumItem.PhotoItem(it) }
                }
                AlbumViewState.Events -> albumRepository.events.map { events ->
                    events.map { AlbumListAdapter.AlbumItem.EventItem(it) }
                }
                AlbumViewState.PhotosByEvent -> albumRepository.getPhotosByEvent(selectedEvent.value)
                    .map { photosByEvent ->
                        photosByEvent.map { AlbumListAdapter.AlbumItem.PhotoItem(it) }
                    }
            }.asLiveData()
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