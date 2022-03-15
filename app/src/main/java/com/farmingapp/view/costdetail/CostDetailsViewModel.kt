package com.farmingapp.view.costdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.*
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

    lateinit var initialData : MutableList<CostModel>

    init {
        getInitialData()
    }

    fun receiveUserAction(action: CostDetailAction) {
        when (action) {
            CostDetailAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val totalAmountList = initialData.sumOf { it.amount.toDouble() }
                        val filterPercentage = totalAmountList.times(0.1)
                        val totalAmount = totalAmountList + filterPercentage
                        val resultList = listOf(
                            GenericResultModel("INFO", "", "Calculated Result"),
                            GenericResultModel("filter_accessories", "Filter & Accessories @ 10%", String.format("%.2f", filterPercentage)),
                            GenericResultModel("total_amount", "Total Amount", String.format("%.2f", totalAmount)),
                        )

                        if (databaseService.farmerDetailDAO().getFarmer().field == FieldDesign.PLAIN.name) {
                            _resultCostSavedStatus.value = ResultCostSavedStatusModel.Saved(resultList, isTerraceField = false)
                        } else {
                            _resultCostSavedStatus.value = ResultCostSavedStatusModel.Saved(resultList)
                        }
                    }
                }
            }
            is CostDetailAction.UpdateCostModel -> {
                initialData.removeAt(action.model.id)
                initialData.add(action.model.id, action.model)
            }
        }
    }

    private fun getInitialData() {
        initialData = mutableListOf(
            CostModel(id = 0, title = "PVC Pipe of Diameter(Main)", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 1, title = "PVC Pipe of Diameter(Sub-Main)", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 2, title = "Lateral Diameter Of", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 3, title = "Dripper Of", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 4, title = "Filter", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 5, title = "No. of Control Valve required", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 6, title = "No. of Flush Valve required", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 7, title = "No. of Elbow", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 8, title = "No. of End Caps for Sub-Main", value = "", quantity = "", rate = "", amount = ""),
            CostModel(id = 9, title = "No. of End Caps for Lateral", value = "", quantity = "", rate = "", amount = "")
        )

        _resultCostSavedStatus.value = ResultCostSavedStatusModel.InitialState(initialData)
    }
}