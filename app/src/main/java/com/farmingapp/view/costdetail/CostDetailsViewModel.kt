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

    private lateinit var initialData : MutableList<CostModel>

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
                getInitialData(action.model.quantity.toInt())
                _resultCostSavedStatus.value = ResultCostSavedStatusModel.Update(initialData, action.position)
            }
        }
    }

    private fun getInitialData(filterCount: Int = 1) {
        initialData = mutableListOf(
            CostModel(
                id = 0,
                title = "PVC Pipe of Diameter(Main)",
                value = preferences.getMainlineDiameter(),
                quantity = preferences.getMainlineLength(),
                rate = preferences.getRateOfMain(),
                amount = getAmount(preferences.getMainlineLength(), preferences.getRateOfMain())
            ), CostModel(
                id = 1,
                title = "PVC Pipe of Diameter(Sub-Main)",
                value = preferences.getSubMainDiameter(),
                quantity = preferences.getSubMainLength(),
                rate = preferences.getRateOfSubMain(),
                amount = getAmount(preferences.getSubMainLength(), preferences.getRateOfSubMain())
            ), CostModel(
                id = 2,
                title = "Lateral Diameter Of",
                value = preferences.getLateralDiameter(),
                quantity = preferences.getLateralLength(),
                rate = preferences.getRateOfLateral(),
                amount = getAmount(preferences.getLateralLength(), preferences.getRateOfLateral())
            ), CostModel(
                id = 3,
                title = "Dripper Of",
                value = preferences.getDripperSize(),
                quantity = preferences.getTotalNumberOfDrippers(),
                rate = preferences.getRateOfEmitter(),
                amount = getAmount(preferences.getTotalNumberOfDrippers(), preferences.getRateOfEmitter())
            ), CostModel(
                id = 4,
                title = "Filter",
                value = preferences.getFilterType(),
                quantity = filterCount.toString(),
                rate = preferences.getRateOfFilter(),
                amount = getAmount(preferences.getRateOfFilter(), filterCount.toString()),
                isUpdatable = true
            ), CostModel(
                id = 5,
                title = "No. of Control Valve required",
                value = preferences.getSubMainLength(),
                quantity = preferences.getQuantityOfControlFlow(),
                rate = preferences.getRateOfControlValve(),
                amount = getAmount(preferences.getQuantityOfControlFlow(), preferences.getRateOfControlValve())
            ), CostModel(
                id = 6,
                title = "No. of Flush Valve required",
                value = preferences.getMainlineLength(),
                quantity = "1",
                rate = preferences.getRateOfFlushValve(),
                amount = getAmount("1", preferences.getRateOfFlushValve())
            ), CostModel(
                id = 7,
                title = "No. of Elbow",
                value = preferences.getMainlineLength(),
                quantity = preferences.getQuantityOfElbow(),
                rate = preferences.getRateOfElbow(),
                amount = getAmount(preferences.getQuantityOfElbow(), preferences.getRateOfElbow())
            ), CostModel(
                id = 8,
                title = "No. of End Caps for Sub-Main",
                value = preferences.getSubMainLength(),
                quantity = preferences.getQuantityOfEndCapsSubMain(),
                rate = preferences.getRateOfEndCapsSubMain(),
                amount = getAmount(preferences.getQuantityOfEndCapsSubMain(), preferences.getRateOfEndCapsSubMain())
            ), CostModel(
                id = 9,
                title = "No. of End Caps for Lateral",
                value = preferences.getLateralLength(),
                quantity = preferences.getQuantityOfEndCapsLateral(),
                rate = preferences.getRateOfEndCapsLateral(),
                amount = getAmount(preferences.getQuantityOfEndCapsLateral(), preferences.getRateOfEndCapsLateral())
            )
        )

        _resultCostSavedStatus.value = ResultCostSavedStatusModel.InitialState(initialData)
    }

    private fun getAmount(valueOne: String, valueTwo: String): String {
        val calculation = valueOne.toDouble() * valueTwo.toDouble()
        return calculation.toString()
    }
}