package com.farmingapp.model

sealed class ResultSavedStatusModel {
    data class Saved(val resultList: List<GenericResultModel>, val isTerraceField: Boolean = true): ResultSavedStatusModel()
    data class Failure(val message: String): ResultSavedStatusModel()
    object Pending: ResultSavedStatusModel()
}
