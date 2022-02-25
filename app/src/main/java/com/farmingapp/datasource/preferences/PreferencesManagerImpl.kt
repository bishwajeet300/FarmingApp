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
        private const val DRIPPER_PER_PLANT = "d_p_p"
        private const val DRIPPER_LATERAL = "dripper_lateral"
        private const val DRIP_NUMBER = "drip_number"
    }
}