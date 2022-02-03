package com.farmingapp.view.cropandwater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmingapp.R
import com.farmingapp.databinding.FragmentCropSelectionWaterCalculationBinding
import com.farmingapp.model.CropSelectionWaterCalculationUserModel
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.UserAction
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CropSelectionWaterCalculationFragment : Fragment() {

    private var _binding: FragmentCropSelectionWaterCalculationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CropSelectionWaterCalculationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCropSelectionWaterCalculationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultSavedStatus.collect { value ->
                    when (value) {
                        is ResultSavedStatusModel.Failure -> {
                            enableViews()
                            Snackbar.make(binding.divider, resources.getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show()
                        }
                        ResultSavedStatusModel.Pending -> {
                            enableViews()
                        }
                        ResultSavedStatusModel.Saved -> {
                            disableViews()
                            // Open Bottom sheet with results
                        }
                    }
                }
            }
        }

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnSubmit.setOnClickListener {
            disableViews()
            viewModel.receiveUserAction(
                UserAction.Submit(
                    CropSelectionWaterCalculationUserModel(
                        cropName = binding.etCropName.text.toString(),
                        soilType = binding.etSoilType.text.toString(),
                        plantDistance = binding.etPlantToPlantDistance.text.toString(),
                        rowDistance = binding.etRowToRowDistance.text.toString(),
                        plantShadowArea = binding.etPlantShadowArea.text.toString(),
                        ePan = binding.etEPan.text.toString(),
                        wettedArea = binding.etWettedArea.text.toString()
                    )
                )
            )
        }
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = false
        binding.etCropName.isEnabled = false
        binding.etSoilType.isEnabled = false
        binding.etEPan.isEnabled = false
        binding.etPlantShadowArea.isEnabled = false
        binding.etPlantToPlantDistance.isEnabled = false
        binding.etRowToRowDistance.isEnabled = false
        binding.etWettedArea.isEnabled = false
    }

    private fun enableViews() {
        binding.btnBack.isEnabled = true
        binding.btnReset.isEnabled = true
        binding.btnSubmit.isEnabled = true
        binding.etCropName.isEnabled = true
        binding.etSoilType.isEnabled = true
        binding.etEPan.isEnabled = true
        binding.etPlantShadowArea.isEnabled = true
        binding.etPlantToPlantDistance.isEnabled = true
        binding.etRowToRowDistance.isEnabled = true
        binding.etWettedArea.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}