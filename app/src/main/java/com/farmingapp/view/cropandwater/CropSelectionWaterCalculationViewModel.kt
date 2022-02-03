package com.farmingapp.view.cropandwater


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.entity.CropSelectionWaterCalculationEntity
import com.farmingapp.model.CropSelectionWaterCalculationUserModel
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CropSelectionWaterCalculationViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    fun receiveUserAction(action: UserAction<CropSelectionWaterCalculationUserModel>) {
        when (action) {
            is UserAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        databaseService.cropSelectionWaterCalculationDAO().insertDetail(
                            CropSelectionWaterCalculationEntity(
                                id = 1,
                                crop_name = action.data.cropName,
                                soil_type = action.data.soilType,
                                plant_distance = action.data.plantDistance,
                                row_distance = action.data.rowDistance,
                                plant_shadow_area = action.data.plantShadowArea,
                                e_pan = action.data.ePan,
                                wetted_area = action.data.wettedArea
                            )
                        )
                    }.also {

                        // Do calculation and push result model

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved
                    }
                }
            }
        }
    }
}