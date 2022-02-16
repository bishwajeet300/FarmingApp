package com.farmingapp.view.sharedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.model.OutputDetailsResultModel
import com.farmingapp.model.ResultFetchStatusModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShareDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultFetchStatus = MutableStateFlow<ResultFetchStatusModel>(ResultFetchStatusModel.Pending)
    val resultFetchStatus: StateFlow<ResultFetchStatusModel> = _resultFetchStatus

    fun receiveUserAction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _resultFetchStatus.value = ResultFetchStatusModel.Success(
                    data = OutputDetailsResultModel(
                        cropName = "",
                        soilType = "",
                        plantDistance = "",
                        rowDistance = "",
                        dripperSize = "",
                        dripperPerPlant = "",
                        lateralDiameter = "",
                        lateralLength = "",
                        mainlineDiameter = "",
                        mainlineLength = "",
                        numberOfLateralSubMain = "",
                        numberOfDripperForSubMain = "",
                        subMainDiameter = "",
                        subMainLength = ""
                    )
                )
            }
        }
    }
}