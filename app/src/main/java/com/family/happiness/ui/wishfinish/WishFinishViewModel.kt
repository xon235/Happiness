package com.family.happiness.ui.wishfinish

import androidx.lifecycle.*
import com.family.happiness.Flag
import com.family.happiness.network.SafeResource
import com.family.happiness.network.request.DeleteWishData
import com.family.happiness.network.request.FinishWishData
import com.family.happiness.repository.UserRepository
import com.family.happiness.repository.WishRepository
import kotlinx.coroutines.launch

class WishFinisViewModel(
    private val userRepository: UserRepository,
    private val wishRepository: WishRepository
) : ViewModel() {

    val users = userRepository.users.asLiveData()

    private val _inputEnabled = MutableLiveData(true)
    val inputEnabled: LiveData<Boolean> = _inputEnabled

    private val _finishFlag = MutableLiveData<Flag<Boolean>>()
    val finishFlag: LiveData<Flag<Boolean>> = _finishFlag

    fun finishWish(finishWishData: FinishWishData) = viewModelScope.launch {
        _inputEnabled.value = false
        when(val resource = wishRepository.finishWish(finishWishData)){
            is SafeResource.Success ->{
                resource.value.wish.let { wishRepository.insertWish(listOf(it)) }
                resource.value.contributors.let { wishRepository.insertContributor(it) }
                _finishFlag.value = Flag(true)
            }
            is SafeResource.Failure -> {
                _finishFlag.value = Flag(false)
            }
        }
        _inputEnabled.value = true
    }
}