package com.example.analysisgame.domain.model

sealed class Resource<out T> {
    object Initial : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
}