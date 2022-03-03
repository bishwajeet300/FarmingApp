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

    fun getDripperLateral(): String
    fun setDripperLateral(dripperLateral: String)

    fun getDripNumber(): String
    fun setDripNumber(dripNumber: String)

    fun getSubMainFlowRate(): String
    fun setSubMainFlowRate(subMainFlowRate: String)

    fun getAverageSubMainFlowRate(): String
    fun setAverageSubMainFlowRate(averageSubMainFlowRate: String)

    // Output dataset
    fun getCropName(): String
    fun setCropName(cropName: String)

    fun getSoilType(): String
    fun setSoilType(soilType: String)

    fun getPlantToPlantDistance(): String
    fun setPlanToPlanDistance(planToPlanDistance: String)

    fun getRowToRowDistance(): String
    fun setRowToRowDistance(rowToRowDistance: String)

    fun getDripperSize(): String
    fun setDripperSize(dripperSize: String)

    fun getDripperPerPlant(): String
    fun setDripperPerPlant(dripperPerPlant: String)

    fun getLateralDiameter(): String
    fun setLateralDiameter(lateralDiameter: String)

    fun getLateralLength(): String
    fun setLateralLength(lateralLength: String)

    fun getMainlineDiameter(): String
    fun setMainlineDiameter(mainlineDiameter: String)

    fun getMainlineLength(): String
    fun setMainlineLength(mainlineLength: String)

    fun getNumberOfLateralSubMain(): String
    fun setNumberOfLateralSubMain(numberOfLateralSubMain: String)

    fun getNumberOfDripperForSubMain(): String
    fun setNumberOfDripperForSubMain(numberOfDripperForSubMain: String)

    fun getSubMainDiameter(): String
    fun setSubMainDiameter(subMainDiameter: String)

    fun getSubMainLength(): String
    fun setSubMainLength(subMainLength: String)
}