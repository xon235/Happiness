package com.family.happiness.ui.photoupload

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.repository.AlbumRepository
import com.family.happiness.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber

class PhotoUploadViewModel(
    private val userRepository: UserRepository,
    private val albumRepository: AlbumRepository,
) : ViewModel() {

    val users = userRepository.users.asLiveData()
    val events = albumRepository.events.asLiveData()

    private val _uploadFinishFlag = MutableLiveData<Flag<Boolean>>()
    val uploadFinishFlag: LiveData<Flag<Boolean>> = _uploadFinishFlag

    private val _inputEnabled = MutableLiveData(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    fun upload(
        isNewEvent: Boolean,
        eventName: String,
        tags: List<String>,
        parts: List<MultipartBody.Part>
    ) = viewModelScope.launch {
        _inputEnabled.value = false
        when(val resource = albumRepository.uploadPhotos(isNewEvent, eventName, tags, parts)){
            is SafeResource.Success ->{
                resource.value.event?.let { albumRepository.insertEvent(listOf(it)) }
                resource.value.photos?.let { albumRepository.insertPhoto(it) }
                _uploadFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                Timber.d(resource.throwable)
                _uploadFinishFlag.value = Flag(false)
            }
        }
        _inputEnabled.value = true
    }
}