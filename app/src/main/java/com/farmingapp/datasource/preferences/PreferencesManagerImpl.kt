package com.farmingapp.datasource.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): PreferencesManager {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)

    override fun getCropWaterRequirement(): String {
        return getValue(CROP_WATER_REQUIREMENT)
    }

    override fun setCropWaterRequirement(cropWaterRequirement: String) {
        setValue(CROP_WATER_REQUIREMENT, cropWaterRequirement)
    }

    override fun getArea(): String {
        return getValue(AREA)
    }

    override fun setArea(area: String) {
        setValue(AREA, area)
    }

    override fun getDripperInternalDiameter(): String {
        return getValue(DRIPPER_INTERNAL_DIAMETER)
    }

    override fun setDripperInternalDiameter(dripperInternalDiameter: String) {
        setValue(DRIPPER_INTERNAL_DIAMETER, dripperInternalDiameter)
    }

    override fun getDripperSpacing(): String {
        return getValue(DRIPPER_SPACING)
    }

    override fun setDripperSpacing(dripperSpacing: String) {
       setValue(DRIPPER_SPACING, dripperSpacing)
    }

    override fun getLateralSpacing(): String {
        return getValue(LATERAL_SPACING)
    }

    override fun setLateralSpacing(lateralSpacing: String) {
        setValue(LATERAL_SPACING, lateralSpacing)
    }

    override fun getTerraceWidths(): String {
        return getValue(TERRACE_WIDTH)
    }

    override fun setTerraceWidths(widths: String) {
        setValue(TERRACE_WIDTH, widths)
    }

    private fun setValue(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    private fun getValue(key: String): String {
        return preferences.getString(key, "").orEmpty()
    }

    override fun getNumberOfLateral(): String {
        return getValue(NUMBER_OF_LATERAL)
    }

    override fun setNumberOfLateral(numberOfLateral: String) {
        setValue(NUMBER_OF_LATERAL, numberOfLateral)
    }

    override fun getLateralFlowRate(): String {
        return getValue(LATERAL_FLOW_RATE)
    }

    override fun setLateralFlowRate(lateralFlowRate: String) {
        setValue(LATERAL_FLOW_RATE, lateralFlowRate)
    }

    override fun getFrictionFactor(): String {
        return getValue(FRICTION_FACTOR)
    }

    override fun setFrictionFactor(frictionFactor: String) {
        setValue(FRICTION_FACTOR, frictionFactor)
    }

    override fun getTotalNumberOfDrippers(): String {
        return getValue(TOTAL_NO_OF_DRIPPERS)
    }

    override fun setTotalNumberOfDrippers(totalNumberOfDrippers: String) {
        setValue(TOTAL_NO_OF_DRIPPERS, totalNumberOfDrippers)
    }

    override fun getDripperPerPlant(): String {
        return getValue(DRIPPER_PER_PLANT)
    }

    override fun setDripperPerPlant(dripperPerPlant: String) {
        setValue(DRIPPER_PER_PLANT, dripperPerPlant)
    }

    override fun getDripperLateral(): String {
        return getValue(DRIPPER_LATERAL)
    }

    override fun setDripperLateral(dripperLateral: String) {
        setValue(DRIPPER_LATERAL, dripperLateral)
    }

    override fun getDripNumber(): String {
        return getValue(DRIP_NUMBER)
    }

    override fun setDripNumber(dripNumber: String) {
        setValue(DRIP_NUMBER, dripNumber)
    }

    override fun getSubMainFlowRate(): String {
        return getValue(SUB_MAIN_FLOW_RATE)
    }

    override fun setSubMainFlowRate(subMainFlowRate: String) {
        setValue(SUB_MAIN_FLOW_RATE, subMainFlowRate)
    }

    override fun getAverageSubMainFlowRate(): String {
        return getValue(AVG_SUB_MAIN_FLOW_RATE)
    }

    override fun setAverageSubMainFlowRate(averageSubMainFlowRate: String) {
        setValue(AVG_SUB_MAIN_FLOW_RATE, averageSubMainFlowRate)
    }

    override fun getCropName(): String {
        return getValue(CROP_NAME)
    }

    override fun setCropName(cropName: String) {
        setValue(cropName, CROP_NAME)
    }

    override fun getSoilType(): String {
        return getValue(SOIL_TYPE)
    }

    override fun setSoilType(soilType: String) {
        setValue(soilType, SOIL_TYPE)
    }

    override fun getPlantToPlantDistance(): String {
        return getValue(PLANT_DISTANCE)
    }

    override fun setPlanToPlanDistance(planToPlanDistance: String) {
        setValue(planToPlanDistance, PLANT_DISTANCE)
    }

    override fun getRowToRowDistance(): String {
        return getValue(ROW_DISTANCE)
    }

    override fun setRowToRowDistance(rowToRowDistance: String) {
        setValue(rowToRowDistance, ROW_DISTANCE)
    }

    override fun getDripperSize(): String {
        return getValue(DRIPPER_SIZE)
    }

    override fun setDripperSize(dripperSize: String) {
        setValue(dripperSize, DRIPPER_SIZE)
    }

    override fun getLateralDiameter(): String {
        return getValue(LATERAL_DIAMETER)
    }

    override fun setLateralDiameter(lateralDiameter: String) {
        setValue(lateralDiameter, LATERAL_DIAMETER)
    }

    override fun getLateralLength(): String {
        return getValue(LATERAL_LENGTH)
    }

    override fun setLateralLength(lateralLength: String) {
        setValue(lateralLength, LATERAL_LENGTH)
    }

    override fun getMainlineDiameter(): String {
        return getValue(MAINLINE_DIAMETER)
    }

    override fun setMainlineDiameter(mainlineDiameter: String) {
        setValue(mainlineDiameter, MAINLINE_DIAMETER)
    }

    override fun getMainlineLength(): String {
        return getValue(MAINLINE_LENGTH)
    }

    override fun setMainlineLength(mainlineLength: String) {
        setValue(mainlineLength, MAINLINE_LENGTH)
    }

    override fun getNumberOfLateralSubMain(): String {
        return getValue(NO_OF_LATERAL_SUBMAIN)
    }

    override fun setNumberOfLateralSubMain(numberOfLateralSubMain: String) {
        setValue(numberOfLateralSubMain, NO_OF_LATERAL_SUBMAIN)
    }

    override fun getNumberOfDripperForSubMain(): String {
        return getValue(NO_OF_DRIPPER_FOR_SUBMAIN)
    }

    override fun setNumberOfDripperForSubMain(numberOfDripperForSubMain: String) {
        setValue(numberOfDripperForSubMain, NO_OF_DRIPPER_FOR_SUBMAIN)
    }

    override fun getSubMainDiameter(): String {
        return getValue(SUB_MAIN_DIAMETER)
    }

    override fun setSubMainDiameter(subMainDiameter: String) {
        setValue(subMainDiameter, SUB_MAIN_DIAMETER)
    }

    override fun getSubMainLength(): String {
        return getValue(SUB_MAIN_LENGTH)
    }

    override fun setSubMainLength(subMainLength: String) {
        setValue(subMainLength, SUB_MAIN_LENGTH)
    }

    companion object {
        private const val PREFERENCES_FILE = "FarmingPrefs"
        private const val CROP_WATER_REQUIREMENT = "c_w_r"
        private const val AREA = "area"
        private const val DRIPPER_INTERNAL_DIAMETER = "d_i_d"
        private const val DRIPPER_SPACING = "dripper_spacing"
        private const val LATERAL_SPACING = "lateral_spacing"
        private const val TERRACE_WIDTH = "terrace_width"
        private const val NUMBER_OF_LATERAL = "n_o_l"
        private const val LATERAL_FLOW_RATE = "flow_rate_lateral"
        private const val FRICTION_FACTOR = "friction_factor"
        private const val TOTAL_NO_OF_DRIPPERS = "t_n_o_d"
        private const val DRIPPER_LATERAL = "dripper_lateral"
        private const val DRIP_NUMBER = "drip_number"
        private const val SUB_MAIN_FLOW_RATE = "sub_main_flow_rate"
        private const val AVG_SUB_MAIN_FLOW_RATE = "avg_sub_main_flow_rate"
        private const val CROP_NAME = "crop_name"
        private const val SOIL_TYPE = "soil_type"
        private const val PLANT_DISTANCE = "plant_distance"
        private const val ROW_DISTANCE = "row_distance"
        private const val DRIPPER_SIZE = "dripper_size"
        private const val DRIPPER_PER_PLANT = "d_p_p"
        private const val LATERAL_DIAMETER = "lateral_diameter"
        private const val LATERAL_LENGTH = "lateral_length"
        private const val MAINLINE_DIAMETER = "mainline_diameter"
        private const val MAINLINE_LENGTH = "mainline_length"
        private const val NO_OF_LATERAL_SUBMAIN = "n_o_l_sm"
        private const val NO_OF_DRIPPER_FOR_SUBMAIN = "n_o_d_f_sm"
        private const val SUB_MAIN_DIAMETER = "sub_main_diameter"
        private const val SUB_MAIN_LENGTH = "sub_main_length"
    }
}