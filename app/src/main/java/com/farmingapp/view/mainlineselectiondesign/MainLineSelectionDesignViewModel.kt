package com.farmingapp.view.mainlineselectiondesign


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.model.*
import com.farmingapp.view.landing.FieldDesign
import com.farmingapp.view.plainfieldsubmainselectiondesign.PlainFieldSubMainSelectionDesignViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainLineSelectionDesignViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var mainLineDiameter: MainLineDiameter

    companion object {
        val mainLineDiameterList = listOf(
            MainLineDiameter(key = "40", label = "40 mm", value = "40"),
            MainLineDiameter(key = "50", label = "50 mm", value = "50"),
            MainLineDiameter(key = "63", label = "63 mm", value = "63"),
            MainLineDiameter(key = "75", label = "75 mm", value = "75"),
            MainLineDiameter(key = "90", label = "90 mm", value = "90"),
            MainLineDiameter(key = "110", label = "110 mm", value = "110")
        )
    }

    fun receiveUserAction(action: MainLineSelectionDesignAction) {
        when (action) {
            is MainLineSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val resultList = listOf(
                            GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"),
                            GenericResultModel("calculated_head_loss", "Calculated Head-Loss (m)", "TBD"),
                            GenericResultModel("discharge_main_line", "Total Discharge of Main-Line (lph)", "TBD"),
                            GenericResultModel("selected_main_line_internal_diameter", "Selected Main-Line Internal Diameter (mm)", "TBD"),
                        )

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is MainLineSelectionDesignAction.SaveOption -> {
                if (action.type == OptionsType.MAIN_DIAMETER) {
                    mainLineDiameter = mainLineDiameterList.first { it.key == action.data.key }
                }
            }
        }
    }
}