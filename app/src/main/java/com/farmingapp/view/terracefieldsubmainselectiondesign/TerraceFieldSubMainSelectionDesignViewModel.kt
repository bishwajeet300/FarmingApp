package com.farmingapp.view.terracefieldsubmainselectiondesign


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.*
import com.farmingapp.view.helper.TransformationUtil
import com.farmingapp.view.landing.FieldDesign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class TerraceFieldSubMainSelectionDesignViewModel @Inject constructor(
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

    fun receiveUserAction(action: TerraceFieldSubMainSelectionDesignAction) {
        when (action) {
            is TerraceFieldSubMainSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val lateralFlowRate = TransformationUtil().transformStringToList(preferences.getLateralFlowRate())
                        val flowRateList = lateralFlowRate.map { it * preferences.getNumberOfLateral().toInt() }

                        val base = 10.0
                        val factor = (1.21 * base.pow(10))/(preferences.getFrictionFactor().toDouble().pow(1.852)).times(subMainDiameter.subMainDiameter.toDouble().pow(-4.871)).times(0.35)
                        val headLossList = flowRateList.zip(action.data.subMainLengthPerTerrace) { x3, x1 -> factor * x3.pow(1.852) * x1 }


                        val resultList = mutableListOf(
                            GenericResultModel("lateral_per_sub_main", "No. of Lateral per Sub-Main", preferences.getNumberOfLateral()),
                            GenericResultModel("flow_rate_sub_main", "Flow rate in Sub-Main (l/s)", TransformationUtil().transformListToString(flowRateList)),
                            GenericResultModel("total_flow_rate_sub_main", "Total Flow rate in Sub-Main (l/s)", String.format("%.4f", flowRateList.sum())),
                            GenericResultModel("head_loss_sub_main", "Head Loss in Sub-Main(m)", TransformationUtil().transformListToString(headLossList)),
                            GenericResultModel("total_head_loss_sub_main", "Total Head Loss in Sub-Main(m)", String.format("%.4f", headLossList.sum())),
                            GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"),
                            GenericResultModel("total_plants", "Total No. of Plants", "${preferences.getTotalNumberOfDrippers().toInt()/preferences.getDripperPerPlant().toInt()}"),
                            GenericResultModel("total_drippers", "Total No. of Drippers", preferences.getTotalNumberOfDrippers()),
                            GenericResultModel("avg_flowrate_sub_main", "Avg Flowrate of Sub-Main", String.format("%.7f", flowRateList.sum())),
                            GenericResultModel("sub_main_diameter", "Selected Sub-Main Diameter (mm)", subMainDiameter.subMainDiameter)
                        )

                        preferences.setAverageSubMainFlowRate(String.format("%.7f", flowRateList.sum()))

                        val widthList = TransformationUtil().transformStringToList(preferences.getTerraceWidths())

                        var messageWarningFlag = false
                        headLossList.forEachIndexed { index, value -> if (widthList[index] > value) { messageWarningFlag = true }}

                        if (messageWarningFlag) {
                            resultList.add(GenericResultModel("INFO", "", "Your selected Sub-Main size is wrong. The Calculated Head Loss is not sufficient to carry the flow. Change the Diameter"))
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
            is TerraceFieldSubMainSelectionDesignAction.SaveOption -> {
                if (action.type == OptionsType.SUB_MAIN_DIAMETER) {
                    subMainDiameter = subMainDiameterList.first { it.key == action.data.key }
                }
            }
        }
    }
}