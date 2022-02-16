package com.farmingapp.model

sealed class ResultFetchStatusModel {
    object Pending: ResultFetchStatusModel()
    data class Success(val data: OutputDetailsResultModel): ResultFetchStatusModel()
    data class Failure(val message: String): ResultFetchStatusModel()
}
