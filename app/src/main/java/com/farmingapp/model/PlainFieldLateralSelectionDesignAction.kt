package com.farmingapp.model

sealed class PlainFieldLateralSelectionDesignAction {
    data class Submit(val data: PlainFieldLateralSelectionDesignUserModel): PlainFieldLateralSelectionDesignAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): PlainFieldLateralSelectionDesignAction()
}
