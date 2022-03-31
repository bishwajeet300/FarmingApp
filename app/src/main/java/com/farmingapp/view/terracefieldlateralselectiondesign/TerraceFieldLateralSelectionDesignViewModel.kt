package com.farmingapp.view.terracefieldlateralselectiondesign


import android.util.Log
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
class TerraceFieldLateralSelectionDesignViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var lateralDiameter: LateralDiameter
    private lateinit var pipeMaterial: PipeMaterial

    companion object {
        val lateralDiameterList = listOf(
            LateralDiameter(key = "12", label = "12 mm", value = "12", internalDiameter = "9.6", rateOfLateral = "18", rateOfEndCapsLateral = "5"),
            LateralDiameter(key = "16", label = "16 mm", value = "16", internalDiameter = "12.7", rateOfLateral = "21", rateOfEndCapsLateral = "8"),
        )

        val pipeMaterialList = listOf(
            PipeMaterial(key = "aluminium", label = "Aluminium", "130"),
            PipeMaterial(key = "brass", label = "Brass", "135"),
            PipeMaterial(key = "cast_iron", label = "Cast Iron", "130"),
            PipeMaterial(key = "concrete", label = "Concrete", "120"),
            PipeMaterial(key = "galvanised_iron", label = "Galvanised Iron", "120"),
            PipeMaterial(key = "hdpe", label = "HDPE", "150"),
            PipeMaterial(key = "smooth_pipes", label = "Smooth Pipes", "140"),
            PipeMaterial(key = "steel", label = "Steel", "145"),
            PipeMaterial(key = "wrought_iron", label = "Wrought Iron", "100")
        )
    }

    fun receiveUserAction(action: TerraceFieldLateralSelectionDesignAction) {
        when (action) {
            is TerraceFieldLateralSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val totalDripperList = action.data.lateralLengthPerTerrace.map {
                                (it * action.data.lateralPerTerrace.toInt()).div(
                                    preferences.getDripperSpacing().toDouble()
                                )
                            }
                            val flowRateList = totalDripperList.map {
                                (it * preferences.getDripperInternalDiameter().toDouble()).div(3600)
                            }
                            val base = 10.0
                            val factor = (1.21 * base.pow(10)) / (pipeMaterial.value.toDouble()
                                .pow(1.852)).times(
                                preferences.getLateralSpacing().toDouble().pow(-4.871)
                            ).times(0.35)

                            val headLossList =
                                flowRateList.zip(action.data.lateralLengthPerTerrace) { x3, x1 ->
                                    factor * x3.pow(1.852) * x1
                                }

                            val lateralFlowRate =
                                TransformationUtil().transformListToString(flowRateList)

                            val resultList = mutableListOf(
                                GenericResultModel("INFO", "", "Calculated Result"),
                                GenericResultModel(
                                    "total_dripper_per_lateral",
                                    "Total No. of Dripper/Lateral/Terrace",
                                    TransformationUtil().transformListToString(totalDripperList)
                                ),
                                GenericResultModel(
                                    "flow_rate_lateral",
                                    "Flow rate of each Lateral/Terrace (l/s)",
                                    lateralFlowRate
                                ),
                                GenericResultModel(
                                    "head_loss_each_terrace",
                                    "Head Loss for each Terrace",
                                    TransformationUtil().transformListToString(headLossList)
                                ),
                                GenericResultModel(
                                    "head_loss",
                                    "Head Loss (m)",
                                    "${headLossList.sum()}"
                                ),
                                GenericResultModel(
                                    "friction_factor",
                                    "Friction Factor",
                                    pipeMaterial.value
                                ),
                                GenericResultModel(
                                    "total_dripper",
                                    "Total Drippers",
                                    "${totalDripperList.sum().times(3)}"
                                ),
                                GenericResultModel(
                                    "outlet_factor",
                                    "Outlet Factor",
                                    "Taken as 0.35"
                                ),
                                GenericResultModel(
                                    "discharge_emitter",
                                    "Discharge of Emitter (lph)",
                                    "${action.data.lateralLengthPerTerrace.sum() * 3}"
                                ),
                                GenericResultModel(
                                    "selected_lateral_internal_diameter",
                                    "Selected Lateral of\nInternal Diameter (mm)",
                                    lateralDiameter.internalDiameter
                                )
                            )

                            preferences.setLateralDiameter(lateralDiameter.label)
                            preferences.setLateralLength(
                                action.data.lateralLengthPerTerrace.sum().toString()
                            )
                            preferences.setRateOfLateral(lateralDiameter.rateOfLateral)
                            preferences.setRateOfEndCapsLateral(lateralDiameter.rateOfEndCapsLateral)

                            val widthList =
                                TransformationUtil().transformStringToList(preferences.getTerraceWidths())

                            var messageWarningFlag = false
                            headLossList.forEachIndexed { index, value ->
                                if (widthList[index] > value) {
                                    messageWarningFlag = true
                                }
                            }

                            if (messageWarningFlag) {
                                resultList.add(
                                    GenericResultModel(
                                        "INFO",
                                        "",
                                        "Your selected Lateral size is wrong. The Calculated Head Loss is not sufficient to carry the flow. Change the Diameter"
                                    )
                                )
                            } else {
                                resultList.add(
                                    GenericResultModel(
                                        "INFO",
                                        "",
                                        "Your selected Sub-lateral size is good. The calculated Head Loss is sufficient to carry the flow. Go To Next"
                                    )
                                )
                            }

                            resultList.add(GenericResultModel("ACTION", "", ""))

                            preferences.setNumberOfLateral(action.data.lateralPerTerrace)
                            preferences.setLateralFlowRate(lateralFlowRate)
                            preferences.setFrictionFactor(pipeMaterial.value)

                            if (databaseService.farmerDetailDAO()
                                    .getFarmer().field == FieldDesign.PLAIN.name
                            ) {
                                _resultSavedStatus.value =
                                    ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                            } else {
                                _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                            }
                        } catch (e: Exception) {
                            Log.e("Terrace_Lateral", e.message.orEmpty())
                        }
                    }
                }
            }
            is TerraceFieldLateralSelectionDesignAction.SaveOption -> {
                if (action.type == OptionsType.LATERAL_DIAMETER) {
                    lateralDiameter = lateralDiameterList.first { it.key == action.data.key }
                }

                if (action.type == OptionsType.PIPE_MATERIAL) {
                    pipeMaterial = pipeMaterialList.first { it.key == action.data.key }
                }
            }
        }
    }
}