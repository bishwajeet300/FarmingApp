package com.farmingapp.view.terracedetails


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.TerraceDetailUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.model.GenericResultModel
import com.farmingapp.model.UserAction
import com.farmingapp.view.landing.FieldDesign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TerraceDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    fun receiveUserAction(action: UserAction<TerraceDetailUserModel>) {
        when (action) {
            is UserAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {

                        // Do calculation and push result model

                        val resultList = listOf(
                            GenericResultModel("area_of_each_terrace", "Area of each Terrace", "TBD m2\nTBD m2\nTBD m2"),
                            GenericResultModel("total_area_of_field", "Total area of field (m2)", "TBD m2"),
                            GenericResultModel("pressure_available", "Pressure Available (kg/cm2)", "TBD kg/cm2"),
                            GenericResultModel("total_pressure", "Total Pressure (kg/cm2)", "TBD kg/cm2"),
                        )

                        _resultSavedStatus.value = ResultSavedStatusModel.Saved(resultList)
                    }
                }
            }
        }
    }
}