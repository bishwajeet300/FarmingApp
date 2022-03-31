package com.farmingapp.model

sealed class DataFetchStatusModel {
    object Pending: DataFetchStatusModel()
    data class Success(val data: FarmerDetailUserModel): DataFetchStatusModel()
    data class Failure(val message: String): DataFetchStatusModel()
}
