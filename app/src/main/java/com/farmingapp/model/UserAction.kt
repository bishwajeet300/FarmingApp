package com.farmingapp.model

sealed class UserAction<out T: Any> {
    data class Submit<out T: Any>(val data: T): UserAction<T>()
    data class SaveOption<out T: Any>(val data: T, val type: OptionsType): UserAction<T>()
}
