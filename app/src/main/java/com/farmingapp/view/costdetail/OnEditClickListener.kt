package com.farmingapp.view.costdetail

import com.farmingapp.model.CostModel

interface OnEditClickListener {
    fun onEditClick(model: CostModel, position: Int)
}