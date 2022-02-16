package com.farmingapp.model

sealed class UserAction<out T: Any> {
    data class Submit<out T: Any>(val data: T): UserAction<T>()
}
