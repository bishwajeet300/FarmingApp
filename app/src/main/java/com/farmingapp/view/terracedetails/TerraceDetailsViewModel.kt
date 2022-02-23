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
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.GenericResultModel
import com.farmingapp.model.UserAction
import com.farmingapp.view.helper.TransformationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TerraceDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
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
                            GenericResultModel("area_of_each_terrace", "Area of each Terrace", TransformationUtil().transformListToString(areaList)),
                            GenericResultModel("total_area_of_field", "Total area of field (m2)", String.format("%.2f", areaList.sum())),
                            GenericResultModel("pressure_available", "Pressure Available (kg/cm2)", TransformationUtil().transformListToString(pressureList)),
                            GenericResultModel("total_pressure", "Total Pressure (kg/cm2)", String.format("%.2f", pressureList.sum())),
                        )
                        preferences.setArea("${areaList.sum()}")
                        preferences.setTerraceWidths(TransformationUtil().transformListToString(action.data.eachTerraceWidth))

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                    }
                }
            }
        }
    }
}