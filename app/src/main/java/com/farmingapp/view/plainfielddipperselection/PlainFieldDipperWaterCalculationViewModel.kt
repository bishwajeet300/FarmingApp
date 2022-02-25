package com.farmingapp.view.plainfielddipperselection


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlainFieldDipperWaterCalculationViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var dripper: DripperModel

    companion object {
        val dripperList = listOf(
            DripperModel(key = "2", label = "2 lph", value = "2", innerDiameter = "1.6"),
            DripperModel(key = "4", label = "4 lph", value = "4", innerDiameter = "3.0"),
            DripperModel(key = "8", label = "8 lph", value = "8", innerDiameter = "6.0")
        )
    }

    fun receiveUserAction(action: PlainFieldDipperWaterCalculationAction) {
        when (action) {
            is PlainFieldDipperWaterCalculationAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val resultList = listOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel(key = "crop_water_requirement", label = "Crop Water Requirement (l/day/plant)", value = preferences.getCropWaterRequirement()),
                            GenericResultModel(key = "total_area_of_field", label = "Area (m2)", value = "${action.data.fieldLength.toDouble() * action.data.fieldWidth.toDouble()}"),
                            GenericResultModel(key = "internal_diameter_drip_selected", label = "Internal diameter of Drip Selected (lph)", value = dripper.innerDiameter),
                            GenericResultModel(key = "total_dripper", label = "Total No. of Dripper", value = String.format("%.2f", (action.data.fieldLength.toDouble() * action.data.fieldWidth.toDouble()).div(preferences.getLateralSpacing().toDouble() * preferences.getDripperSpacing().toDouble()))),
                            GenericResultModel(key = "irrigation_time", label = "Irrigation Time (hr)", value = String.format("%.2f", preferences.getCropWaterRequirement().toDouble()/dripper.innerDiameter.toDouble()))
                        )
                        preferences.setLateralSpacing(action.data.lateralSpacing)
                        preferences.setDripperSpacing(action.data.dripperSpacing)
                        preferences.setDripNumber(action.data.dripperPerPlant)

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, true)
                    }
                }
            }
            is PlainFieldDipperWaterCalculationAction.SaveOption -> {
                dripper = dripperList.first { it.key == action.data.key }
            }
        }
    }
}