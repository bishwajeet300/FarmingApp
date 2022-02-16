package com.farmingapp.model

sealed class SystemWaterSourceDetailsAction {
    object Submit: SystemWaterSourceDetailsAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): SystemWaterSourceDetailsAction()
}
