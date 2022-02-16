package com.farmingapp.model

sealed class PlainFieldSubMainSelectionDesignAction {
    data class Submit(val data: PlainFieldSubMainSelectionDesignUserModel): PlainFieldSubMainSelectionDesignAction()
    data class SaveOption(val data: GenericOptionModel, val type: OptionsType): PlainFieldSubMainSelectionDesignAction()
}
