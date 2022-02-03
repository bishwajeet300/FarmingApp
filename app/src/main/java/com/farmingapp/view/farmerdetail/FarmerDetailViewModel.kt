package com.farmingapp.view.farmerdetail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.datasource.entity.FarmerEntity
import com.farmingapp.model.FarmerDetailUserModel
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FarmerDetailViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    fun receiveUserAction(action: UserAction<FarmerDetailUserModel>) {
        when (action) {
            is UserAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        databaseService.farmerDetailDAO().insertFarmer(
                            FarmerEntity(
                                id = 1,
                                full_name = action.data.name,
                                address = action.data.address,
                                phone = action.data.phone,
                                email = action.data.email,
                                field = action.data.field
                            )
                        )
                    }.also {
                        _resultSavedStatus.value = ResultSavedStatusModel.Saved
                    }
                }
            }
        }

    }
}