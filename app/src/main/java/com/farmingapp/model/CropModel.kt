package com.farmingapp.model

data class CropModel(
    val key: String,
    val label: String,
    val coefficient: Double,
    val scientificName: String?,
    val climateRequirements: String?,
    val soilRequirements: String?,
    val waterRequirements: String?,
    val harvesting: String?,
    val fieldPreparation: String?,
    val sowingTime: String?,
    val transplantTime: String?,
    val isDetailsVisible: Boolean = false
)
