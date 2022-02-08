package com.farmingapp.model

sealed class CropSelectionWaterCalculationAction {
    data class Submit(val data: CropSelectionWaterCalculationUserModel): CropSelectionWaterCalculationAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): CropSelectionWaterCalculationAction()
}
