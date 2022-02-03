package com.farmingapp.model

data class CropSelectionWaterCalculationUserModel(
    val cropName: String,
    val soilType: String,
    val plantDistance: String,
    val rowDistance: String,
    val plantShadowArea: String,
    val ePan: String,
    val wettedArea: String
)
