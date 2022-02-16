package com.farmingapp.view.outputdetail
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.farmingapp.R
import com.farmingapp.databinding.FragmentOutputDetailsBinding
import com.farmingapp.model.OutputDetailsResultModel
import com.farmingapp.model.ResultFetchStatusModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OutputDetailsFragment : Fragment() {

    private var _binding: FragmentOutputDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OutputDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutputDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultFetchStatus.collect { value ->
                    when (value) {
                        is ResultFetchStatusModel.Failure -> {
                            Snackbar.make(binding.divider, resources.getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show()
                        }
                        ResultFetchStatusModel.Pending -> {

                        }
                        is ResultFetchStatusModel.Success -> {
                            setFormValues(value.data)
                        }
                    }
                }
            }
        }

        setupClickListener()

        viewModel.receiveUserAction()
    }

    private fun setFormValues(data: OutputDetailsResultModel) {
        binding.etCropName.setText(data.cropName)
        binding.etSoilType.setText(data.soilType)
        binding.etPlantToPlantDistance.setText(data.plantDistance)
        binding.etRowToRowDistance.setText(data.rowDistance)
        binding.etSelectedDripperSize.setText(data.dripperSize)
        binding.etDripperPerPlant.setText(data.dripperPerPlant)
        binding.etSelectedLateralDiameter.setText(data.lateralDiameter)
        binding.etLengthOfLateral.setText(data.lateralLength)
        binding.etMainlineDiameter.setText(data.mainlineDiameter)
        binding.etMainlineLength.setText(data.mainlineLength)
        binding.etNumberOfLateralOnSubMain.setText(data.numberOfLateralSubMain)
        binding.etNumberOfDripperForSubMain.setText(data.numberOfDripperForSubMain)
        binding.etSelectedSubMainDiameter.setText(data.subMainDiameter)
        binding.etLengthOfSubMain.setText(data.subMainLength)
    }

    private fun setupClickListener() {
        binding.btnSubmit.setOnClickListener {
            val action = OutputDetailsFragmentDirections.actionOutputDetailsFragmentToCostDetailsFragment2()
            findNavController().navigate(action)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}