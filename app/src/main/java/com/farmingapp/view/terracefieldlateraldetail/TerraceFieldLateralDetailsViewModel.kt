package com.farmingapp.view.terracefieldlateraldetail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farmingapp.datasource.DatabaseService
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.TerraceFieldLateralDetailUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.farmingapp.model.UserAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TerraceFieldLateralDetailsViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultSavedStatus = MutableStateFlow<ResultSavedStatusModel>(ResultSavedStatusModel.Pending)
    val resultSavedStatus: StateFlow<ResultSavedStatusModel> = _resultSavedStatus

    fun receiveUserAction(action: UserAction<TerraceFieldLateralDetailUserModel>) {
        when (action) {
            is UserAction.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                    }
                }
            }
        }
    }
}