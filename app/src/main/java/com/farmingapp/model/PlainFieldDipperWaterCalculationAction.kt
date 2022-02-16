package com.farmingapp.model

sealed class PlainFieldDipperWaterCalculationAction {
    data class Submit(val data: PlainFieldDipperWaterCalculationUserModel): PlainFieldDipperWaterCalculationAction()
        data class SaveOption(val data: GenericOptionModel, val type: OptionsType): PlainFieldDipperWaterCalculationAction()
}
