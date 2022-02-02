package com.farmingapp.model

sealed class ResultSavedStatusModel {
    object Saved: ResultSavedStatusModel()
    data class Failure(val message: String): ResultSavedStatusModel()
    object Pending: ResultSavedStatusModel()
}
