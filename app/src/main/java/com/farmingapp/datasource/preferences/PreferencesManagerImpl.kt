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

    private fun setValue(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    private fun getValue(key: String): String {
        return preferences.getString(key, "").orEmpty()
    }

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

    override fun getPressurePerLateral(): String {
        return getValue(PRESSURE_PER_LATERAL)
    }

    override fun setPressurePerLateral(pressurePerLateral: String) {
        setValue(PRESSURE_PER_LATERAL, pressurePerLateral)
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
        setValue(CROP_NAME, cropName)
    }

    override fun getSoilType(): String {
        return getValue(SOIL_TYPE)
    }

    override fun setSoilType(soilType: String) {
        setValue(SOIL_TYPE, soilType)
    }

    override fun getPlantToPlantDistance(): String {
        return getValue(PLANT_DISTANCE)
    }

    override fun setPlanToPlanDistance(planToPlanDistance: String) {
        setValue(PLANT_DISTANCE, planToPlanDistance)
    }

    override fun getRowToRowDistance(): String {
        return getValue(ROW_DISTANCE)
    }

    override fun setRowToRowDistance(rowToRowDistance: String) {
        setValue(ROW_DISTANCE, rowToRowDistance)
    }

    override fun getDripperSize(): String {
        return getValue(DRIPPER_SIZE)
    }

    override fun setDripperSize(dripperSize: String) {
        setValue(DRIPPER_SIZE, dripperSize)
    }

    override fun getLateralDiameter(): String {
        return getValue(LATERAL_DIAMETER)
    }

    override fun setLateralDiameter(lateralDiameter: String) {
        setValue(LATERAL_DIAMETER, lateralDiameter)
    }

    override fun getLateralLength(): String {
        return getValue(LATERAL_LENGTH)
    }

    override fun setLateralLength(lateralLength: String) {
        setValue(LATERAL_LENGTH, lateralLength)
    }

    override fun getMainlineDiameter(): String {
        return getValue(MAINLINE_DIAMETER)
    }

    override fun setMainlineDiameter(mainlineDiameter: String) {
        setValue(MAINLINE_DIAMETER, mainlineDiameter)
    }

    override fun getMainlineLength(): String {
        return getValue(MAINLINE_LENGTH)
    }

    override fun setMainlineLength(mainlineLength: String) {
        setValue(MAINLINE_LENGTH, mainlineLength)
    }

    override fun getNumberOfLateralSubMain(): String {
        return getValue(NO_OF_LATERAL_SUBMAIN)
    }

    override fun setNumberOfLateralSubMain(numberOfLateralSubMain: String) {
        setValue(NO_OF_LATERAL_SUBMAIN, numberOfLateralSubMain)
    }

    override fun getNumberOfDripperForSubMain(): String {
        return getValue(NO_OF_DRIPPER_FOR_SUBMAIN)
    }

    override fun setNumberOfDripperForSubMain(numberOfDripperForSubMain: String) {
        setValue(NO_OF_DRIPPER_FOR_SUBMAIN, numberOfDripperForSubMain)
    }

    override fun getSubMainDiameter(): String {
        return getValue(SUB_MAIN_DIAMETER)
    }

    override fun setSubMainDiameter(subMainDiameter: String) {
        setValue(SUB_MAIN_DIAMETER, subMainDiameter)
    }

    override fun getSubMainLength(): String {
        return getValue(SUB_MAIN_LENGTH)
    }

    override fun setSubMainLength(subMainLength: String) {
        setValue(SUB_MAIN_LENGTH, subMainLength)
    }

    override fun getRateOfMain(): String {
        return getValue(RATE_OF_MAIN)
    }

    override fun setRateOfMain(rateOfMain: String) {
        setValue(RATE_OF_MAIN, rateOfMain)
    }

    override fun getFilterType(): String {
        return getValue(FILTER_TYPE)
    }

    override fun setFilterType(filterType: String) {
        setValue(FILTER_TYPE, filterType)
    }

    override fun getRateOfSubMain(): String {
        return getValue(RATE_OF_SUB_MAIN)
    }

    override fun setRateOfSubMain(rateOfSubMain: String) {
        setValue(RATE_OF_SUB_MAIN, rateOfSubMain)
    }

    override fun getRateOfControlValve(): String {
        return getValue(RATE_OF_CONTROL_VALVE)
    }

    override fun setRateOfControlValve(rateOfControlValve: String) {
        setValue(RATE_OF_CONTROL_VALVE, rateOfControlValve)
    }

    override fun getRateOfFlushValve(): String {
        return getValue(RATE_OF_FLUSH_VALVE)
    }

    override fun setRateOfFlushValve(rateOfFlushValve: String) {
        setValue(RATE_OF_FLUSH_VALVE, rateOfFlushValve)
    }

    override fun getRateOfElbow(): String {
        return getValue(RATE_OF_ELBOW)
    }

    override fun setRateOfElbow(rateOfElbow: String) {
        setValue(RATE_OF_ELBOW, rateOfElbow)
    }

    override fun getRateOfEndCapsSubMain(): String {
        return getValue(RATE_OF_END_CAPS_SUB_MAIN)
    }

    override fun setRateOfEndCapsSubMain(rateOfEndCapsSubMain: String) {
        setValue(RATE_OF_END_CAPS_SUB_MAIN, rateOfEndCapsSubMain)
    }

    override fun getRateOfLateral(): String {
        return getValue(RATE_OF_LATERAL)
    }

    override fun setRateOfLateral(rateOfLateral: String) {
        setValue(RATE_OF_LATERAL, rateOfLateral)
    }

    override fun getRateOfEndCapsLateral(): String {
        return getValue(RATE_OF_END_CAPS_LATERAL)
    }

    override fun setRateOfEndCapsLateral(rateOfEndCapsLateral: String) {
        setValue(RATE_OF_END_CAPS_LATERAL, rateOfEndCapsLateral)
    }

    override fun getRateOfEmitter(): String {
        return getValue(RATE_OF_EMITTER)
    }

    override fun setRateOfEmitter(rateOfEmitter: String) {
        setValue(RATE_OF_EMITTER, rateOfEmitter)
    }

    override fun getRateOfFilter(): String {
        return getValue(RATE_OF_FILTER)
    }

    override fun setRateOfFilter(rateOfFilter: String) {
        setValue(RATE_OF_FILTER, rateOfFilter)
    }

    override fun getQuantityOfControlFlow(): String {
        return getValue(QUANTITY_OF_CONTROL_FLOW)
    }

    override fun setQuantityOfControlFlow(quantityOfControlFlow: String) {
        setValue(QUANTITY_OF_CONTROL_FLOW, quantityOfControlFlow)
    }

    override fun getQuantityOfElbow(): String {
        return getValue(QUANTITY_OF_ELBOW)
    }

    override fun setQuantityOfElbow(quantityOfElbow: String) {
        setValue(QUANTITY_OF_ELBOW, quantityOfElbow)
    }

    override fun getQuantityOfEndCapsSubMain(): String {
        return getValue(QUANTITY_OF_END_CAPS_SUB_MAIN)
    }

    override fun setQuantityOfEndCapsSubMain(quantityOfEndCapsSubMain: String) {
        setValue(QUANTITY_OF_END_CAPS_SUB_MAIN, quantityOfEndCapsSubMain)
    }

    override fun getQuantityOfEndCapsLateral(): String {
        return getValue(QUANTITY_OF_END_CAPS_LATERAL)
    }

    override fun setQuantityOfEndCapsLateral(quantityOfEndCapsLateral: String) {
        setValue(QUANTITY_OF_END_CAPS_LATERAL, quantityOfEndCapsLateral)
    }

    companion object {
        private const val PREFERENCES_FILE = "FarmingPrefs"
        private const val CROP_WATER_REQUIREMENT = "c_w_r"
        private const val AREA = "area"
        private const val DRIPPER_INTERNAL_DIAMETER = "d_i_d"
        private const val DRIPPER_SPACING = "dripper_spacing"
        private const val LATERAL_SPACING = "lateral_spacing"
        private const val PRESSURE_PER_LATERAL = "pressure_per_lateral"
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
        private const val RATE_OF_MAIN = "rate_of_main"
        private const val FILTER_TYPE = "filter_type"
        private const val RATE_OF_SUB_MAIN = "rate_of_sub_main"
        private const val RATE_OF_CONTROL_VALVE = "rate_of_control_valve"
        private const val RATE_OF_FLUSH_VALVE = "rate_of_flush_valve"
        private const val RATE_OF_ELBOW = "rate_of_elbow"
        private const val RATE_OF_END_CAPS_SUB_MAIN = "rate_of_end_caps_sub_main"
        private const val RATE_OF_LATERAL = "rate_of_lateral"
        private const val RATE_OF_END_CAPS_LATERAL = "rate_of_end_caps_lateral"
        private const val RATE_OF_EMITTER = "rate_of_emitter"
        private const val RATE_OF_FILTER = "rate_of_filter"
        private const val QUANTITY_OF_CONTROL_FLOW = "quantity_of_control_flow"
        private const val QUANTITY_OF_ELBOW = "quantity_of_elbow"
        private const val QUANTITY_OF_END_CAPS_SUB_MAIN = "quantity_of_end_caps_sub_main"
        private const val QUANTITY_OF_END_CAPS_LATERAL = "quantity_of_end_caps_lateral"
    }
}