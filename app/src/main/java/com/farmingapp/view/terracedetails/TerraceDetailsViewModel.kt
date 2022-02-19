package com.farmingapp.view.terracedetails


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.TerraceDetailUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.model.GenericResultModel
import com.farmingapp.model.UserAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TerraceDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    fun receiveUserAction(action: UserAction<TerraceDetailUserModel>) {
        when (action) {
            is UserAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {

                        // Do calculation and push result model
                        val areaList = action.data.eachTerraceLength.zip(action.data.eachTerraceWidth) { length, width -> length * width }
                        val pressureList = action.data.eachTerraceHeight.map { it/10 }

                        val resultList = listOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel("area_of_each_terrace", "Area of each Terrace", transformListForDisplay(areaList)),
                            GenericResultModel("total_area_of_field", "Total area of field (m2)", "${areaList.sum()}"),
                            GenericResultModel("pressure_available", "Pressure Available (kg/cm2)", transformListForDisplay(pressureList)),
                            GenericResultModel("total_pressure", "Total Pressure (kg/cm2)", "${pressureList.sum()}"),
                        )

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                    }
                }
            }
        }
    }

    private fun transformListForDisplay(list: List<Double>): String {
        val result = StringBuilder()

        for (value in list) {
            if (result.isEmpty()) {
                result.append(value)
            } else {
                result.append("\n").append(value)
            }
        }
        return result.toString()
    }
}