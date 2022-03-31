package com.farmingapp.view.systemwaterdetails


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.*
import com.farmingapp.view.landing.FieldDesign
import com.farmingapp.view.mainlineselectiondesign.MainLineSelectionDesignViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SystemWaterSourceDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var waterSource: WaterSource
    private lateinit var waterTankLocation: WaterTankLocation

    companion object {
        val waterSourceList = listOf(
            WaterSource(key = "spring", label = "Spring", value = "spring", filter = "Screen Filter", rateOfFilter = "3500"),
            WaterSource(key = "river_nala", label = "River/Nala", value = "river_nala", filter = "Screen & Disk Filter", rateOfFilter = "7000"),
            WaterSource(key = "tube_well", label = "Tube Well", value = "tube_well", filter = "Screen Filter", rateOfFilter = "3500"),
            WaterSource(key = "pond_lake", label = "Pond/Lake", value = "pond_lake", filter = "Screen Filter", rateOfFilter = "3500"),
            WaterSource(key = "open_well", label = "Open Well", value = "open_well", filter = "Screen & Disk Filter", rateOfFilter = "7000"),
        )

        val waterTankLocationList = listOf(
            WaterTankLocation(key = "center", label = "Center", value = "center"),
            WaterTankLocation(key = "corner", label = "Corner", value = "corner")
        )
    }

    fun receiveUserAction(action: SystemWaterSourceDetailsAction) {
        when (action) {
            is SystemWaterSourceDetailsAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val resultList = listOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel("suggested_filter", "Suggested Filter", waterSource.filter),
                            GenericResultModel("ACTION", "", "")
                        )
                        preferences.setFilterType(waterSource.filter)
                        preferences.setRateOfFilter(waterSource.rateOfFilter)

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is SystemWaterSourceDetailsAction.SaveOption -> {
                if (action.type == OptionsType.WATER_SOURCE) {
                    waterSource = waterSourceList.first { it.key == action.data.key }
                }

                if (action.type == OptionsType.WATER_TANK_LOCATION) {
                    waterTankLocation = waterTankLocationList.first { it.key == action.data.key }
                }
            }
        }
    }
}