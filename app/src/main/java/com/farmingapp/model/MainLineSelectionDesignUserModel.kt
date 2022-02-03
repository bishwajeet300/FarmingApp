package com.farmingapp.model

data class MainLineSelectionDesignUserModel(
    val mainlineDiameter: String,
    val mainlineLength: String,
    val outletFactor: String,
    val calculatedHeadLoss: String,
    val mainlineTotalDischarge: String
)
