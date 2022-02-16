package com.farmingapp.view.plainfieldsubmainselectiondesign


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.model.*
import com.farmingapp.view.landing.FieldDesign
import com.farmingapp.view.terracefieldsubmainselectiondesign.TerraceFieldSubMainSelectionDesignViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlainFieldSubMainSelectionDesignViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var subMainDiameter: SubMainDiameter

    companion object {
        val subMainDiameterList = listOf(
            SubMainDiameter(key = "32", label = "32 mm", value = "32"),
            SubMainDiameter(key = "40", label = "40 mm", value = "40"),
            SubMainDiameter(key = "50", label = "50 mm", value = "50"),
            SubMainDiameter(key = "63", label = "63 mm", value = "63"),
            SubMainDiameter(key = "75", label = "75 mm", value = "75")
        )
    }

    fun receiveUserAction(action: PlainFieldSubMainSelectionDesignAction) {
        when (action) {
            is PlainFieldSubMainSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val resultList = listOf(
                            GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"),
                            GenericResultModel("total_plants", "Total No. of Plants", "TBD"),
                            GenericResultModel("total_drippers", "Total No. of Drippers", "TBD"),
                            GenericResultModel("total_lateral_per_sub_main", "Total No. of Lateral per Sub-Main", "TBD"),
                            GenericResultModel("flow_rate_sub_main", "Flow rate in Sub-Main", "TBD"),
                            GenericResultModel("head_loss_sub_main", "Head Loss in Sub-Main(m)", "TBD"),
                        )

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is PlainFieldSubMainSelectionDesignAction.SaveOption -> {
                if (action.type == OptionsType.SUB_MAIN_DIAMETER) {
                    subMainDiameter = subMainDiameterList.first { it.key == action.data.key }
                }
            }
        }
    }
}