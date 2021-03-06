package com.farmingapp.view.terracefieldlateraldetail


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
class TerraceFieldLateralDetailsViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var dripper: DripperModel

    companion object {
        val dripperList = listOf(
            DripperModel(key = "2", label = "2 lph", value = "2", innerDiameter = "1.6", rateOfEmitter = "3.5"),
            DripperModel(key = "4", label = "4 lph", value = "4", innerDiameter = "3.0", rateOfEmitter = "5"),
        )
    }

    fun receiveUserAction(action: TerraceFieldLateralDetailsAction) {
        when (action) {
            is TerraceFieldLateralDetailsAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val totalNumberOfDrippers = (preferences.getArea().toDouble()/(action.data.dripperSpacing.toDouble() * action.data.lateralSpacing.toDouble())).toInt()

                        val resultList = listOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel(key = "crop_water_requirement", label = "Crop Water Requirement (lt/day/plant)", preferences.getCropWaterRequirement()),
                            GenericResultModel(key = "area", label = "Area (m2)", value = preferences.getArea()),
                            GenericResultModel(key = "drip_selected", label = "Drip Selected(lph) (Inner Diameter)", value = dripper.innerDiameter),
                            GenericResultModel(key = "total_no_dripper", label = "Total No. of Dripper", value = "$totalNumberOfDrippers"),
                            GenericResultModel(key = "irrigation_time", label = "Irrigation Time(hr)", value = String.format("%.2f", preferences.getCropWaterRequirement().toDouble()/dripper.innerDiameter.toDouble())),
                            GenericResultModel("ACTION", "", "")
                        )
                        preferences.setDripperInternalDiameter(dripper.innerDiameter)
                        preferences.setDripperSpacing(action.data.dripperSpacing)
                        preferences.setLateralSpacing(action.data.lateralSpacing)
                        preferences.setTotalNumberOfDrippers("$totalNumberOfDrippers")
                        preferences.setDripNumber(action.data.dripperPerPlant)
                        preferences.setDripperPerPlant(action.data.dripperPerPlant)
                        preferences.setDripperSize(dripper.label)
                        preferences.setRateOfEmitter(dripper.rateOfEmitter)

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, true)
                    }
                }
            }
            is TerraceFieldLateralDetailsAction.SaveOption -> {
                dripper = dripperList.first { it.key == action.data.key }
            }
        }
    }
}