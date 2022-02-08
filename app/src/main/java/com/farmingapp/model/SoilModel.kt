package com.farmingapp.model

data class SoilModel(
    val key: String,
    val label: String,
    val hydraulicConductivity: Double,
    val porosity: Double,
    val capacity: Double,
    val wiltingPoint: Double
)
