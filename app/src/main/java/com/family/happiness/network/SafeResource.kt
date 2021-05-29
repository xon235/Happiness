package com.family.happiness.network

import timber.log.Timber

sealed class SafeResource<out T> {
    data class Success<out T>(val value: T) : SafeResource<T>()
    data class Failure(val throwable: Throwable) : SafeResource<Nothing>(){
        init {
            Timber.d(throwable)
        }
    }
}