package com.farmingapp.view.cropandwater


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.entity.CropSelectionWaterCalculationEntity
import com.farmingapp.model.*
import com.farmingapp.view.landing.FieldDesign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CropSelectionWaterCalculationViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var crop: CropModel
    private lateinit var soil: SoilModel

    companion object {
        val cropList = listOf(
            CropModel("wheat", "Wheat", 0.675),
            CropModel("bean", "Bean", 0.7625),
            CropModel("cabbage", "Cabbage", 0.7875),
            CropModel("carrot", "Carrot", 0.7875),
            CropModel("cotton", "Cotton", 0.775),
            CropModel("cucumber", "Cucumber", 0.7),
            CropModel("squash", "Squash", 0.7),
            CropModel("tomato", "Tomato", 0.7875),
            CropModel("pulses", "Pulses", 0.7),
            CropModel("lentil", "Lentil", 0.7),
            CropModel("spinach", "Spinach", 0.7375),
            CropModel("maize", "Maize", 0.8375),
            CropModel("millet", "Millet", 0.7),
            CropModel("onion", "Onion", 0.8),
            CropModel("peanut", "Peanut", 0.7375),
            CropModel("groundnut", "Groundnut", 0.7375),
            CropModel("pepper", "Pepper", 0.75),
            CropModel("potato", "Potato", 0.8),
            CropModel("radish", "Radish", 0.7125),
            CropModel("sorghum", "Sorghum", 0.7125),
            CropModel("soyabean", "Soyabean", 0.7),
            CropModel("sugarbeet", "Sugarbeet", 0.8),
            CropModel("sunflower", "Sunflower", 0.7),
            CropModel("tobacco", "Tobacco", 0.775)
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
                                e_pan = action.data.ePan,
                                wetted_area = action.data.wettedArea
                            )
                        )
                        // Do calculation and push result model

                        val resultList = listOf(
                            GenericResultModel("crop_factor", "Crop Factor", crop.coefficient.toString()),
                            GenericResultModel("crop_canopy", "Crop Canopy", "TBD"),
                            GenericResultModel("area_each_plant", "Area for each plant", "TBD"),
                            GenericResultModel("system_efficiency", "System Efficiency (%)", "Taken as 85%"),
                            GenericResultModel("k_pan", "K-Pan", "Taken as 0.7"),
                            GenericResultModel("eto", "ETo (mm/day)", "TBD"),
                            GenericResultModel("etc", "ETc (mm/day)", "TBD"),
                            GenericResultModel("e_pan", "E-Pan (mm/day)", action.data.ePan),
                            GenericResultModel("crop_water_requirement", "Crop Water Requirement (lt/day/plant)", "TBD"),
                            GenericResultModel("INFO", "", "Crop Information"),
                            GenericResultModel("INFO", "", "Soil Information")
                        )

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is CropSelectionWaterCalculationAction.SaveOption -> {
                when (action.type) {
                    OptionsType.SOIL -> {
                        soil = soilList.first { it.key == action.data.key }
                    }
                    OptionsType.CROP -> {
                        crop = cropList.first { it.key == action.data.key }
                    }
                }
            }
        }
    }
}