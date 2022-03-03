package com.farmingapp.view.plainfieldsubmainselectiondesign


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
import com.farmingapp.view.terracefieldsubmainselectiondesign.TerraceFieldSubMainSelectionDesignViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class PlainFieldSubMainSelectionDesignViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var subMainDiameter: SubMainDiameter

    companion object {
        val subMainDiameterList = listOf(
            SubMainDiameter(key = "32", label = "32 mm", value = "32", subMainDiameter = "28.7"),
            SubMainDiameter(key = "40", label = "40 mm", value = "40", subMainDiameter = "36.7"),
            SubMainDiameter(key = "50", label = "50 mm", value = "50", subMainDiameter = "45.8"),
            SubMainDiameter(key = "63", label = "63 mm", value = "63", subMainDiameter = "58"),
            SubMainDiameter(key = "75", label = "75 mm", value = "75", subMainDiameter = "69")
        )
    }

    fun receiveUserAction(action: PlainFieldSubMainSelectionDesignAction) {
        when (action) {
            is PlainFieldSubMainSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val flowRate = (action.data.subMainLength.toDouble().div(preferences.getLateralSpacing().toDouble())).times(2).times(preferences.getLateralFlowRate().toDouble())
                        val base = 10.0
                        var headLossFactor = 1.21 * base.pow(10) * action.data.subMainLength.toDouble() * 0.35 * (flowRate / 150).pow(1.852)
                        headLossFactor *= subMainDiameter.subMainDiameter.toDouble().pow(-4.871)

                        val resultList = mutableListOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"),
                            GenericResultModel("total_plants", "Total No. of Plants", String.format("%.4f", preferences.getDripperLateral().toDouble().div(preferences.getDripNumber().toDouble()))),
                            GenericResultModel("total_drippers", "Total No. of Drippers", preferences.getDripperLateral()),
                            GenericResultModel("total_lateral_per_sub_main", "Total No. of Lateral per Sub-Main", String.format("%.4f", action.data.subMainLength.toDouble().div(preferences.getLateralSpacing().toDouble()).times(2))),
                            GenericResultModel("flow_rate_sub_main", "Flow rate in Sub-Main", String.format("%.4f", flowRate)),
                            GenericResultModel("head_loss_sub_main", "Head Loss in Sub-Main(m)", String.format("%.4f", headLossFactor)),
                            GenericResultModel("sub_main_diameter", "Selected Sub-Main Diameter(mm)", subMainDiameter.subMainDiameter)
                        )

                        preferences.setSubMainFlowRate(String.format("%.4f", flowRate))
                        preferences.setNumberOfLateralSubMain(String.format("%.4f", action.data.subMainLength.toDouble().div(preferences.getLateralSpacing().toDouble()).times(2)))
                        preferences.setNumberOfDripperForSubMain(String.format("%.4f", action.data.subMainLength.toDouble().div(preferences.getLateralSpacing().toDouble()).times(2)))
                        preferences.setSubMainDiameter(subMainDiameter.label)
                        preferences.setSubMainLength(action.data.subMainLength)

                        if (headLossFactor > 2) {
                            resultList.add(GenericResultModel("INFO", "", "Your selected Sub-Main size is wrong. The calculated Head Loss is not sufficient to carry the flow. Change the Diameter"))
                        } else {
                            resultList.add(GenericResultModel("INFO", "", "Your selected Sub-Main size is good. The calculated Head Loss is sufficient to carry the flow. Go To Next"))
                        }

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