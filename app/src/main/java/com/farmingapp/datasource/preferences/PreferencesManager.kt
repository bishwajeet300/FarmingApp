package com.farmingapp.datasource.preferences

interface PreferencesManager {
    fun getCropWaterRequirement(): String
    fun setCropWaterRequirement(cropWaterRequirement: String)

    fun getArea(): String
    fun setArea(area: String)

    fun getDripperInternalDiameter(): String
    fun setDripperInternalDiameter(dripperInternalDiameter: String)

    fun getDripperSpacing(): String
    fun setDripperSpacing(dripperSpacing: String)

    fun getLateralSpacing(): String
    fun setLateralSpacing(lateralSpacing: String)

    fun getTerraceWidths(): String
    fun setTerraceWidths(widths: String)

    fun getNumberOfLateral(): String
    fun setNumberOfLateral(numberOfLateral: String)

    fun getLateralFlowRate(): String
    fun setLateralFlowRate(lateralFlowRate: String)

    fun getFrictionFactor(): String
    fun setFrictionFactor(frictionFactor: String)

    fun getTotalNumberOfDrippers(): String
    fun setTotalNumberOfDrippers(totalNumberOfDrippers: String)

    fun getDripperPerPlant(): String
    fun setDripperPerPlant(dripperPerPlant: String)

    fun getDripperLateral(): String
    fun setDripperLateral(dripperLateral: String)

    fun getDripNumber(): String
    fun setDripNumber(dripNumber: String)

    fun getSubMainFlowRate(): String
    fun setSubMainFlowRate(subMainFlowRate: String)

    fun getAverageSubMainFlowRate(): String
    fun setAverageSubMainFlowRate(averageSubMainFlowRate: String)
}