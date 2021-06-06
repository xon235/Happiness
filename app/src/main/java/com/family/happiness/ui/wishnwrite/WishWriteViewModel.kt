package com.family.happiness.ui.wishnwrite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.DeleteWishData
import com.family.happiness.network.request.WriteWishData
import com.family.happiness.repository.WishRepository
import com.family.happiness.room.wish.Wish
import kotlinx.coroutines.launch
import timber.log.Timber

class WishWriteViewModel(private val wishRepository: WishRepository): ViewModel() {

    private val _inputEnabled = MutableLiveData(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _writeFinishFlag = MutableLiveData<Flag<Boolean>>()
    val writeFinishFlag: LiveData<Flag<Boolean>> = _writeFinishFlag


    fun writeWish(writeWishData: WriteWishData) = viewModelScope.launch {
        _inputEnabled.value = false
        when(val resource = wishRepository.writeWish(writeWishData)){
            is SafeResource.Success ->{
                _writeFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _writeFinishFlag.value = Flag(false)
            }
        }
        _inputEnabled.value = true
    }

    private val _deleteFinishFlag = MutableLiveData<Flag<Boolean>>()
    val deleteFinishFlag: LiveData<Flag<Boolean>> = _deleteFinishFlag

    fun deleteWish(deleteWishData: DeleteWishData) = viewModelScope.launch {
        _inputEnabled.value = false
        when(val resource = wishRepository.deleteWish(deleteWishData)){
            is SafeResource.Success ->{
                _deleteFinishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _deleteFinishFlag.value = Flag(false)
            }
        }
        _inputEnabled.value = true
    }
}