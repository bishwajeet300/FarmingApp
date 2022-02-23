package com.farmingapp.view.cropandwater


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.entity.CropSelectionWaterCalculationEntity
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.*
import com.farmingapp.view.landing.FieldDesign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToLong

@HiltViewModel
class CropSelectionWaterCalculationViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var crop: CropModel
    private lateinit var soil: SoilModel
    private lateinit var ePan: EPanModel

    companion object {
        val cropList = listOf(
            CropModel(key = "bean", label = "Bean", coefficient = 0.7625, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "broccoli", label = "Broccoli", coefficient = 0.7875, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "cabbage", label = "Cabbage", coefficient = 0.7875, scientificName = "Brassica Oleracea var. Capitata", climateRequirements = "Averages temperature of 25°C", soilRequirements = "pH 6.0 - pH 6.6", waterRequirements = null, harvesting = "Done when the heads are firm.", fieldPreparation = null, sowingTime = null, transplantTime = null, isDetailsVisible = true),
            CropModel(key = "carrot", label = "Carrot", coefficient = 0.7875, scientificName = "Daucus Carota", climateRequirements = "20°C To 25°C", soilRequirements = "Below pH 6.5", waterRequirements = "Irrigate the field once in 8-10 days", harvesting = "Should be harvested at the proper stage of maturity", fieldPreparation = null, sowingTime = null, transplantTime = null, isDetailsVisible = true),
            CropModel(key = "cauliflower", label = "Cauliflower", coefficient = 0.7875, scientificName = "Brassica Oleracea var. Botrytis", climateRequirements = "15°C To 20°C", soilRequirements = "pH 6.0- pH 6.6", waterRequirements = "5-8 irrigation are generally required For cauliflower crop.", harvesting = "Should be done in the morning Or evening.", fieldPreparation = null, sowingTime = "Jul-Sept", transplantTime = "Sept-Oct", isDetailsVisible = true),
            CropModel(key = "cotton", label = "Cotton", coefficient = 0.775, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "cucumber", label = "Cucumber", coefficient = 0.7, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "groundnut", label = "Groundnut", coefficient = 0.7375, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "lentil", label = "Lentil", coefficient = 0.7, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "maize", label = "Maize", coefficient = 0.8375, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "millet", label = "Millet", coefficient = 0.7, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "onion", label = "Onion", coefficient = 0.8, scientificName = "Allium Cepa", climateRequirements = "12°C To 25°C", soilRequirements = "pH 5.8 - pH 6.5", waterRequirements = "Moisture context of the soil should be kept optimum", harvesting = null, fieldPreparation = "Seeds are sown in lines 4-5 cm apart", sowingTime = null, transplantTime = null, isDetailsVisible = true),
            CropModel(key = "peanut", label = "Peanut", coefficient = 0.7375, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "pepper", label = "Pepper", coefficient = 0.75, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "potato", label = "Potato", coefficient = 0.8, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "pulses", label = "Pulses", coefficient = 0.7, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "radish", label = "Radish", coefficient = 0.7125, scientificName = "Raphanus Sativus L", climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null, isDetailsVisible = true),
            CropModel(key = "sorghum", label = "Sorghum", coefficient = 0.7125, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "soyabean", label = "Soyabean", coefficient = 0.7, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "spinach", label = "Spinach", coefficient = 0.7375, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "squash", label = "Squash", coefficient = 0.7, scientificName = "Sechium Edule", climateRequirements = "20°C To 25°C", soilRequirements = "pH 5 - pH 6", waterRequirements = null, harvesting = "One plant can produce more than 300 fruits per year", fieldPreparation = "It Is mainly grown in rainy season", sowingTime = null, transplantTime = null, isDetailsVisible = true),
            CropModel(key = "sugarbeet", label = "Sugarbeet", coefficient = 0.8, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "sunflower", label = "Sunflower", coefficient = 0.7, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "tobacco", label = "Tobacco", coefficient = 0.775, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
            CropModel(key = "tomato", label = "Tomato", coefficient = 0.7875, scientificName = "Lycopersicon Esculentum", climateRequirements = "18°C To 27°C.", soilRequirements = "pH 5.5 - pH 7.5", waterRequirements = "Every 10 days", harvesting = null, fieldPreparation = "spacing of 3X2.5ft under polyhouse And 2.5X2ft", sowingTime = null, transplantTime = null, isDetailsVisible = true),
            CropModel(key = "wheat", label = "Wheat", coefficient = 0.675, scientificName = null, climateRequirements = null, soilRequirements = null, waterRequirements = null, harvesting = null, fieldPreparation = null, sowingTime = null, transplantTime = null),
        )

        val soilList = listOf(
            SoilModel("sand", "Sand", 120.4, 0.437, 0.062, 0.024),
            SoilModel("loamy_sand", "Loamy Sand", 29.97, 0.437, 0.105, 0.047),
            SoilModel("sandy_loam", "Sandy Loam", 10.92, 0.453, 0.19, 0.085),
            SoilModel("loam", "Loam", 3.30, 0.463, 0.232, 0.116),
            SoilModel("silt_loam", "Silt Loam", 6.60, 0.501, 0.284, 0.135),
            SoilModel("sandy_clay_loam", "Sandy Clay Loam", 1.52, 0.398, 0.244, 0.136),
            SoilModel("clay_loam", "Clay Loam", 1.02, 0.464, 0.31, 0.187),
            SoilModel("silty_clay_loam", "Silty Clay Loam", 1.02, 0.471, 0.342, 0.21),
            SoilModel("sandy_clay", "Sandy Clay", 0.508, 0.43, 0.321, 0.221),
            SoilModel("silty_clay", "Silty Clay", 0.508, 0.479, 0.371, 0.251),
            SoilModel("clay", "Clay", 0.254, 0.475, 0.378, 0.635)
        )

        val ePanList = listOf(
            EPanModel(key = "1", label = "1", value = 1),
            EPanModel(key = "2", label = "2", value = 2),
            EPanModel(key = "3", label = "3", value = 3),
            EPanModel(key = "4", label = "4", value = 4),
            EPanModel(key = "5", label = "5", value = 5),
            EPanModel(key = "6", label = "6", value = 6)
        )
    }

    fun receiveUserAction(action: CropSelectionWaterCalculationAction) {
        when (action) {
            is CropSelectionWaterCalculationAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        databaseService.cropSelectionWaterCalculationDAO().insertDetail(
                            CropSelectionWaterCalculationEntity(
                                id = 1,
                                crop_name = crop.key,
                                soil_type = soil.key,
                                plant_distance = action.data.plantDistance,
                                row_distance = action.data.rowDistance,
                                plant_shadow_area = action.data.plantShadowArea,
                                e_pan = ePan.key,
                                wetted_area = action.data.wettedArea
                            )
                        )
                        // Do calculation and push result model

                        val resultList = mutableListOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel("crop_factor", "Crop Factor", crop.coefficient.toString()),
                            GenericResultModel("crop_canopy", "Crop Canopy", String.format("%.2f", action.data.plantShadowArea.toDouble()/(action.data.plantDistance.toDouble() * action.data.rowDistance.toDouble()))),
                            GenericResultModel("area_each_plant", "Area for each plant", String.format("%.2f", action.data.plantDistance.toDouble() * action.data.rowDistance.toDouble())),
                            GenericResultModel("system_efficiency", "System Efficiency (%)", "Taken as 85%"),
                            GenericResultModel("k_pan", "K-Pan", "Taken as 0.7"),
                            GenericResultModel("eto", "ETo (mm/day)", String.format("%.2f", 0.7 * ePan.value)),
                            GenericResultModel("etc", "ETc (mm/day)", String.format("%.2f", 0.7 * ePan.value * crop.coefficient)),
                            GenericResultModel("e_pan", "E-Pan (mm/day)", ePan.key)
                        )

                        val waterRequirement = String.format("%.2f", (crop.coefficient * 0.7 * ePan.value * action.data.plantDistance.toDouble() * action.data.rowDistance.toDouble() * action.data.wettedArea.toDouble()) / 0.85)

                        resultList.add(GenericResultModel("crop_water_requirement", "Crop Water Requirement (lt/day/plant)", waterRequirement))
                        preferences.setCropWaterRequirement(waterRequirement)

                        // Add Crop Details
                        if (crop.isDetailsVisible) {
                            resultList.add(GenericResultModel("INFO", "", "Information On Crop"))
                            if (crop.scientificName != null) {
                                resultList.add(GenericResultModel("s_scientific_name", "Scientific Name", crop.scientificName!!))
                            }
                            if (crop.climateRequirements != null) {
                                resultList.add(GenericResultModel("s_climate_requirements", "Climate Requirements", crop.climateRequirements!!))
                            }
                            if (crop.soilRequirements != null) {
                                resultList.add(GenericResultModel("s_soil_requirements", "Soil Requirements", crop.soilRequirements!!))
                            }
                            if (crop.waterRequirements != null) {
                                resultList.add(GenericResultModel("s_water_requirements", "Water Requirements", crop.waterRequirements!!))
                            }
                            if (crop.harvesting != null) {
                                resultList.add(GenericResultModel("s_harvesting", "Harvesting", crop.harvesting!!))
                            }
                            if (crop.fieldPreparation != null) {
                                resultList.add(GenericResultModel("s_field_preparation", "Field Preparation", crop.fieldPreparation!!))
                            }
                            if (crop.sowingTime != null) {
                                resultList.add(GenericResultModel("s_sowing_time", "Sowing Time", crop.sowingTime!!))
                            }
                            if (crop.transplantTime != null) {
                                resultList.add(GenericResultModel("s_transplant_time", "Transplant Time", crop.transplantTime!!))
                            }
                        }

                        // Add Soil Details
                        resultList.addAll(listOf(
                            GenericResultModel("INFO", "", "Information On Soil - ${soil.label}"),
                            GenericResultModel("s_hydraulic_conductivity", "Hydraulic Conductivity (mm/hr)", soil.hydraulicConductivity.toString()),
                            GenericResultModel("s_porosity", "Porosity", soil.porosity.toString()),
                            GenericResultModel("s_capacity", "Field Capacity", soil.capacity.toString()),
                            GenericResultModel("s_wilting_point", "Wilting Point", soil.wiltingPoint.toString())
                        ))

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is CropSelectionWaterCalculationAction.SaveOption -> {
                if (action.type == OptionsType.SOIL) {
                    soil = soilList.first { it.key == action.data.key }
                }

                if (action.type == OptionsType.CROP) {
                    crop = cropList.first { it.key == action.data.key }
                }

                if (action.type == OptionsType.EPAN) {
                    ePan = ePanList.first { it.key == action.data.key }
                }
            }
        }
    }
}