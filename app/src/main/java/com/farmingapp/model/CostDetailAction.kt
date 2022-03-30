package com.farmingapp.model

sealed class CostDetailAction {
    data class UpdateCostModel(val model: CostModel, val position: Int): CostDetailAction()
    object Submit: CostDetailAction()
}
