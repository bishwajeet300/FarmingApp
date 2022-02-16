package com.farmingapp.model

sealed class TerraceFieldLateralSelectionDesignAction {
    data class Submit(val data: TerraceFieldLateralSelectionDesignUserModel): TerraceFieldLateralSelectionDesignAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): TerraceFieldLateralSelectionDesignAction()
}
