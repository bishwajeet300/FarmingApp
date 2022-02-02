package com.farmingapp.view.costdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.farmingapp.model.ResultSavedStatusModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CostDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus
}