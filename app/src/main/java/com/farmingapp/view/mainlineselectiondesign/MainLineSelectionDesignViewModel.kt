package com.farmingapp.view.mainlineselectiondesign


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.preferences.PreferencesManager
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
import kotlin.math.pow

@HiltViewModel
class MainLineSelectionDesignViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var mainLineDiameter: MainLineDiameter

    companion object {
        val mainLineDiameterList = listOf(
            MainLineDiameter(key = "40", label = "40 mm", value = "40", internalDiameter = "36.6"),
            MainLineDiameter(key = "50", label = "50 mm", value = "50", internalDiameter = "45.8"),
            MainLineDiameter(key = "63", label = "63 mm", value = "63", internalDiameter = "58"),
            MainLineDiameter(key = "75", label = "75 mm", value = "75", internalDiameter = "69"),
            MainLineDiameter(key = "90", label = "90 mm", value = "90", internalDiameter = "82.8"),
            MainLineDiameter(key = "110", label = "110 mm", value = "110", internalDiameter = "101.3"),
        )
    }

    fun receiveUserAction(action: MainLineSelectionDesignAction) {
        when (action) {
            is MainLineSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        var dsm = 0.0
                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            dsm = preferences.getSubMainFlowRate().toDouble()
                        } else {
                            dsm = preferences.getAverageSubMainFlowRate().toDouble()
                        }

                        val base = 10.0
                        var headLossFactor = 1.21 * base.pow(10) * action.data.mainlineLength.toDouble() * 0.35 * (dsm / 150).pow(1.852)
                        headLossFactor *= mainLineDiameter.internalDiameter.toDouble().pow(-4.871)
                        val resultList = mutableListOf<GenericResultModel>()

                        preferences.setMainlineDiameter(mainLineDiameter.label)
                        preferences.setMainlineLength(action.data.mainlineLength)

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            resultList.add(GenericResultModel("INFO", "", "Calculated Result"))
                            resultList.add(GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"))
                            resultList.add(GenericResultModel("calculated_head_loss", "Calculated Head-Loss (m)", String.format("%.4f", headLossFactor)))
                            resultList.add(GenericResultModel("discharge_main_line", "Total Discharge of Main-Line (lph)", "$dsm"))
                            resultList.add(GenericResultModel("selected_main_line_internal_diameter", "Selected Main-Line Internal Diameter (mm)", mainLineDiameter.internalDiameter))
                            if (headLossFactor > 1.3) {
                                resultList.add(GenericResultModel("INFO", "", "Your selected Main size is wrong. The calculated Head Loss is not sufficient to carry the flow. Change the Diameter"))
                            } else {
                                resultList.add(GenericResultModel("INFO", "", "Your selected Main size is good. The calculated Head Loss is sufficient to carry the flow. Go To Next"))
                            }
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            resultList.add(GenericResultModel("INFO", "", "Calculated Result"))
                            resultList.add(GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"))
                            resultList.add(GenericResultModel("calculated_head_loss", "Calculated Head-Loss (m)", String.format("%.4f", headLossFactor)))
                            resultList.add(GenericResultModel("discharge_main_line", "Total Discharge of Main-Line (lph)", "$dsm"))
                            resultList.add(GenericResultModel("selected_main_line_internal_diameter", "Selected Main-Line Internal Diameter (mm)", mainLineDiameter.internalDiameter))
                            if (headLossFactor > 1.3) {
                                resultList.add(GenericResultModel("INFO", "", "Your selected Main size is wrong. The calculated Head Loss is not sufficient to carry the flow. Change the Diameter"))
                            } else {
                                resultList.add(GenericResultModel("INFO", "", "Your selected Main size is good. The calculated Head Loss is sufficient to carry the flow. Go To Next"))
                            }
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