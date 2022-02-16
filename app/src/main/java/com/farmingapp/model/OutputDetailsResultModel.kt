package com.farmingapp.model

data class OutputDetailsResultModel(
    val cropName: String,
    val soilType: String,
    val plantDistance: String,
    val rowDistance: String,
    val dripperSize: String,
    val dripperPerPlant: String,
    val lateralDiameter: String,
    val lateralLength: String,
    val mainlineDiameter: String,
    val mainlineLength: String,
    val numberOfLateralSubMain: String,
    val numberOfDripperForSubMain: String,
    val subMainDiameter: String,
    val subMainLength: String
)
