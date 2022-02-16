package com.farmingapp.view.terracefieldlateraldetail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TerraceFieldLateralDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    private lateinit var dripper: DripperModel

    companion object {
        val dripperList = listOf(
            DripperModel(key = "2", label = "2 lph", value = "2"),
            DripperModel(key = "4", label = "4 lph", value = "4"),
            DripperModel(key = "8", label = "8 lph", value = "8")
        )
    }

    fun receiveUserAction(action: TerraceFieldLateralDetailsAction) {
        when (action) {
            is TerraceFieldLateralDetailsAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val resultList = listOf(
                            GenericResultModel(key = "area_of_each_terrace", label = "Area of each terrace (m2)", value = "TBD"),
                            GenericResultModel(key = "total_area_of_field", label = "Total area of field (m2)", value = "TBD"),
                            GenericResultModel(key = "pressure_available", label = "Pressure available (kg/cm2)", value = "TBD"),
                            GenericResultModel(key = "total_pressure", label = "Total pressure (kg/cm2)", value = "TBD")
                        )

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList, true)
                    }
                }
            }
            is TerraceFieldLateralDetailsAction.SaveOption -> {
                dripper = dripperList.first { it.key == action.data.key }
            }
        }
    }
}