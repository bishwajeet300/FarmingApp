package com.farmingapp.model

sealed class ResultCostSavedStatusModel {
    data class Saved(val resultList: List<GenericResultModel>, val isTerraceField: Boolean = true): ResultCostSavedStatusModel()
    data class Failure(val message: String): ResultCostSavedStatusModel()
    object Pending: ResultCostSavedStatusModel()
    data class InitialState(val dataList: List<CostModel>): ResultCostSavedStatusModel()
}
