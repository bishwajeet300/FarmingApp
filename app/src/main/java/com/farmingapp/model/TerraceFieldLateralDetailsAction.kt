package com.farmingapp.model

sealed class TerraceFieldLateralDetailsAction {
    data class Submit(val data: TerraceFieldLateralDetailUserModel): TerraceFieldLateralDetailsAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): TerraceFieldLateralDetailsAction()
}
