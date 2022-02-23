package com.farmingapp.view.plainfieldlateralselectiondesign


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.model.*
import com.farmingapp.view.landing.FieldDesign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlainFieldLateralSelectionDesignViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var lateralDiameter: LateralDiameter
    private lateinit var pipeMaterial: PipeMaterial

    companion object {
        val lateralDiameterList = listOf(
            LateralDiameter(key = "12", label = "12 mm", value = "12", internalDiameter = "9.6"),
            LateralDiameter(key = "16", label = "16 mm", value = "16", internalDiameter = "12.7"),
            LateralDiameter(key = "20", label = "20 mm", value = "20", internalDiameter = "16.5")
        )

        val pipeMaterialList = listOf(
            PipeMaterial(key = "aluminium", label = "Aluminium", "130"),
            PipeMaterial(key = "brass", label = "Brass", "135"),
            PipeMaterial(key = "cast_iron", label = "Cast Iron", "130"),
            PipeMaterial(key = "concrete", label = "Concrete", "120"),
            PipeMaterial(key = "galvanised_iron", label = "Galvanised Iron", "120"),
            PipeMaterial(key = "pvc", label = "PVC", "150"),
            PipeMaterial(key = "smooth_pipes", label = "Smooth Pipes", "140"),
            PipeMaterial(key = "steel", label = "Steel", "145"),
            PipeMaterial(key = "wrought_iron", label = "Wrought Iron", "100"),
        )
    }

    fun receiveUserAction(action: PlainFieldLateralSelectionDesignAction) {
        when (action) {
            is PlainFieldLateralSelectionDesignAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val resultList = listOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel("flow_rate_lateral", "Flow rate of each Lateral", "TBD"),
                            GenericResultModel("total_dripper_per_lateral", "Total No. of Dripper/Lateral", "TBD"),
                            GenericResultModel("head_loss", "Head Loss (m)", "TBD"),
                            GenericResultModel("friction_factor", "Friction Factor", "TBD"),
                            GenericResultModel("outlet_factor", "Outlet Factor", "Taken as 0.35"),
                            GenericResultModel("selected_lateral_internal_diameter", "Selected Lateral of\nInternal Diameter (mm)", "TBD"),
                            GenericResultModel("discharge_emitter", "Discharge of Emitter (lph)", "TBD"),
                            GenericResultModel("length_lateral", "Total length of Lateral", "TBD")
                        )

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is PlainFieldLateralSelectionDesignAction.SaveOption -> {
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