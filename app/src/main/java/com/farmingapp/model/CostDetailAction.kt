package com.farmingapp.model

sealed class CostDetailAction {
    data class UpdateCostModel(val model: CostModel): CostDetailAction()
    object Submit: CostDetailAction()
}
