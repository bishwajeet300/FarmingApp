package com.farmingapp.model

sealed class MainLineSelectionDesignAction {
    data class Submit(val data: MainLineSelectionDesignUserModel): MainLineSelectionDesignAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): MainLineSelectionDesignAction()
}
