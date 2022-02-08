package com.farmingapp.view.helper

import com.farmingapp.model.GenericOptionModel
import com.farmingapp.model.OptionsType

interface OnOptionsClickListener {
    fun onOptionsClick(type: OptionsType, model: GenericOptionModel)
}