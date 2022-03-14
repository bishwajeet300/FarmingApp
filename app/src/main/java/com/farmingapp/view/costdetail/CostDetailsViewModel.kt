package com.farmingapp.view.costdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.CostModel
import com.farmingapp.model.GenericResultModel
import com.farmingapp.model.ResultCostSavedStatusModel
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.view.landing.FieldDesign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CostDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultCostSavedStatus = MutableStateFlow<ResultCostSavedStatusModel>(ResultCostSavedStatusModel.Pending)
    val resultCostSavedStatus: StateFlow<ResultCostSavedStatusModel> = _resultCostSavedStatus

    init {
        getInitialData()
    }

    fun receiveUserAction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val resultList = listOf(
                    GenericResultModel("INFO", "", "Calculated Result"),
                    GenericResultModel("filter_accessories", "Filter & Accessories @ 10%", ""),
                    GenericResultModel("total_amount", "Total Amount", ""),
                )

                if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                    _resultCostSavedStatus.value = ResultCostSavedStatusModel.Saved(resultList, isTerraceField = false)
                } else {
                    _resultCostSavedStatus.value = ResultCostSavedStatusModel.Saved(resultList)
                }
            }
        }
    }

    private fun getInitialData() {
        val initialData = listOf(
            CostModel(title = "PVC Pipe of Diameter(Main)", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "PVC Pipe of Diameter(Sub-Main)", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "Lateral Diameter Of", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "Dripper Of", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "Filter", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "No. of Control Valve required", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "No. of Flush Valve required", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "No. of Elbow", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "No. of End Caps for Sub-Main", value = "", quantity = "", rate = "", amount = ""),
            CostModel(title = "No. of End Caps for Lateral", value = "", quantity = "", rate = "", amount = "")
        )

        _resultCostSavedStatus.value = ResultCostSavedStatusModel.InitialState(initialData)
    }
}