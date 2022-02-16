package com.farmingapp.model

sealed class TerraceFieldSubMainSelectionDesignAction {
    data class Submit(val data: TerraceFieldSubMainSelectionDesignUserModel): TerraceFieldSubMainSelectionDesignAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): TerraceFieldSubMainSelectionDesignAction()
}
