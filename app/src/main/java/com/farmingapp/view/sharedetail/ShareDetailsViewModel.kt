package com.farmingapp.view.sharedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.preferences.PreferencesManager
import com.farmingapp.model.DataFetchStatusModel
import com.farmingapp.model.FarmerDetailUserModel
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
    private val preferences: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultFetchStatus = MutableStateFlow<ResultFetchStatusModel>(ResultFetchStatusModel.Pending)
    val resultFetchStatus: StateFlow<ResultFetchStatusModel> = _resultFetchStatus

    private val _dataFetchStatus = MutableStateFlow<DataFetchStatusModel>(DataFetchStatusModel.Pending)
    val dataFetchStatus: StateFlow<DataFetchStatusModel> = _dataFetchStatus

    init {
        fetchData()
    }

    fun receiveUserAction() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _resultFetchStatus.value = ResultFetchStatusModel.Success(
                    data = OutputDetailsResultModel(
                        cropName = preferences.getCropName(),
                        soilType = preferences.getSoilType(),
                        plantDistance = preferences.getPlantToPlantDistance(),
                        rowDistance = preferences.getRowToRowDistance(),
                        dripperSize = preferences.getDripperSize(),
                        dripperPerPlant = preferences.getDripperPerPlant(),
                        lateralDiameter = preferences.getLateralDiameter(),
                        lateralLength = preferences.getLateralLength(),
                        mainlineDiameter = preferences.getMainlineDiameter(),
                        mainlineLength = preferences.getMainlineLength(),
                        numberOfLateralSubMain = preferences.getNumberOfLateralSubMain(),
                        numberOfDripperForSubMain = preferences.getNumberOfDripperForSubMain(),
                        subMainDiameter = preferences.getSubMainDiameter(),
                        subMainLength = preferences.getSubMainLength(),
                    )
                )
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val farmer = databaseService.farmerDetailDAO().getFarmer()

                _dataFetchStatus.value = DataFetchStatusModel.Success(
                    data = FarmerDetailUserModel(
                        name = farmer.full_name,
                        phone = farmer.phone,
                        email = farmer.email,
                        address = farmer.address,
                        field = farmer.field
                    )
                )
            }
        }
    }
}