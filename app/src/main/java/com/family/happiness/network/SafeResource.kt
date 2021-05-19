package com.family.happiness.network

sealed class SafeResource<out T> {
    data class Success<out T>(val value: T) : SafeResource<T>()
    data class Failure(val throwable: Throwable) : SafeResource<Nothing>()
}