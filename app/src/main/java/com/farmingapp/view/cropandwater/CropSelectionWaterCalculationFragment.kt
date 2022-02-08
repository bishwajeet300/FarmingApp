package com.farmingapp.view.cropandwater
import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmingapp.R
import com.farmingapp.databinding.BottomsheetOptionsBinding
import com.farmingapp.databinding.BottomsheetResultBinding
import com.farmingapp.databinding.FragmentCropSelectionWaterCalculationBinding
import com.farmingapp.model.*
import com.farmingapp.view.helper.OnOptionsClickListener
import com.farmingapp.view.helper.OptionsAdapter
import com.farmingapp.view.helper.ResultAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CropSelectionWaterCalculationFragment : Fragment(), OnOptionsClickListener {

    private var _binding: FragmentCropSelectionWaterCalculationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CropSelectionWaterCalculationViewModel by viewModels()
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var bottomSheetCropDialog: BottomSheetDialog
    private lateinit var bottomSheetSoilDialog: BottomSheetDialog
    private lateinit var bottomSheetEPanDialog: BottomSheetDialog
    private lateinit var resultAdapter: ResultAdapter
    private lateinit var cropOptionsAdapter: OptionsAdapter
    private lateinit var soilOptionsAdapter: OptionsAdapter


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
                        is ResultSavedStatusModel.Saved -> {
                            disableViews()
                            setupResultBottomSheet(value.resultList, value.isTerraceField)
                        }
                    }
                }
            }
        }

        setupSoilOptions()
        setupCropOptions()
        setupClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSoilOptions() {
        bottomSheetSoilDialog = BottomSheetDialog(requireContext())
        val soilBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetSoilDialog.setContentView(soilBottomSheetBinding.root)

        soilOptionsAdapter = OptionsAdapter(OptionsType.SOIL, getSoilList(), this)
        soilBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        soilBottomSheetBinding.rvOptions.adapter = soilOptionsAdapter

        binding.etSoilType.setOnTouchListener { v, _ ->
            bottomSheetSoilDialog.show()
            v.performClick()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCropOptions() {
        bottomSheetCropDialog = BottomSheetDialog(requireContext())
        val cropBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetCropDialog.setContentView(cropBottomSheetBinding.root)

        cropOptionsAdapter = OptionsAdapter(OptionsType.CROP, getCropList(), this)
        cropBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        cropBottomSheetBinding.rvOptions.adapter = cropOptionsAdapter

        binding.etCropName.setOnTouchListener { v, _ ->
            bottomSheetCropDialog.show()
            v.performClick()
        }
    }

    private fun setupResultBottomSheet(resultList: List<GenericResultModel>, isTerraceField: Boolean) {
        bottomSheetResultDialog = BottomSheetDialog(requireContext())
        val resultBottomSheetBinding = BottomsheetResultBinding.inflate(layoutInflater, null, false)
        bottomSheetResultDialog.setContentView(resultBottomSheetBinding.root)
        bottomSheetResultDialog.setCancelable(false)
        bottomSheetResultDialog.setCanceledOnTouchOutside(false)

        resultAdapter = ResultAdapter(resultList)
        resultBottomSheetBinding.rvResult.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        resultBottomSheetBinding.rvResult.adapter = resultAdapter

        resultBottomSheetBinding.btnNext.setOnClickListener {
            if (bottomSheetResultDialog.isShowing) {
                bottomSheetResultDialog.setCancelable(true)
                bottomSheetResultDialog.dismiss()
            }
            if (isTerraceField) {
                val action = CropSelectionWaterCalculationFragmentDirections.actionCropSelectionWaterCalculationFragmentToTerraceDetailsFragment()
                findNavController().navigate(action)
            } else {
                val action = CropSelectionWaterCalculationFragmentDirections.actionCropSelectionWaterCalculationFragmentToPlainFieldDipperWaterCalculationFragment()
                findNavController().navigate(action)
            }
        }

        if (bottomSheetResultDialog.isShowing.not()) {
            bottomSheetResultDialog.show()
        }
    }

    private fun setupClickListener() {
        binding.btnSubmit.setOnClickListener {
            disableViews()
            viewModel.receiveUserAction(
                CropSelectionWaterCalculationAction.Submit(
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

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onOptionsClick(type: OptionsType, model: GenericOptionModel) {
        when (type) {
            OptionsType.SOIL -> {
                binding.etSoilType.setText(model.label)
                if (bottomSheetSoilDialog.isShowing) {
                    bottomSheetSoilDialog.dismiss()
                }
            }
            OptionsType.CROP -> {
                binding.etCropName.setText(model.label)
                if (bottomSheetCropDialog.isShowing) {
                    bottomSheetCropDialog.dismiss()
                }
            }
        }

        viewModel.receiveUserAction(
            CropSelectionWaterCalculationAction.SaveOption(
                model, type
            )
        )
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

    private fun getCropList(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel("wheat", "Wheat"),
            GenericOptionModel("bean", "Bean"),
            GenericOptionModel("cabbage", "Cabbage"),
            GenericOptionModel("carrot", "Carrot"),
            GenericOptionModel("cotton", "Cotton"),
            GenericOptionModel("cucumber", "Cucumber"),
            GenericOptionModel("squash", "Squash"),
            GenericOptionModel("tomato", "Tomato"),
            GenericOptionModel("pulses", "Pulses"),
            GenericOptionModel("lentil", "Lentil"),
            GenericOptionModel("spinach", "Spinach"),
            GenericOptionModel("maize", "Maize"),
            GenericOptionModel("millet", "Millet"),
            GenericOptionModel("onion", "Onion"),
            GenericOptionModel("peanut", "Peanut"),
            GenericOptionModel("groundnut", "Groundnut"),
            GenericOptionModel("pepper", "Pepper"),
            GenericOptionModel("potato", "Potato"),
            GenericOptionModel("radish", "Radish"),
            GenericOptionModel("sorghum", "Sorghum"),
            GenericOptionModel("soyabean", "Soy Bean"),
            GenericOptionModel("sugarbeet", "Sugar Beet"),
            GenericOptionModel("sunflower", "Sunflower"),
            GenericOptionModel("tobacco", "Tobacco")
        )
    }

    private fun getSoilList(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel("sand", "Sand"),
            GenericOptionModel("loamy_sand", "Loamy Sand"),
            GenericOptionModel("sandy_loam", "Sandy Loam"),
            GenericOptionModel("loam", "Loam"),
            GenericOptionModel("silt_loam", "Silt Loam"),
            GenericOptionModel("sandy_clay_loam", "Sandy Clay Loam"),
            GenericOptionModel("clay_loam", "Clay Loam"),
            GenericOptionModel("silty_clay_loam", "Silty Clay Loam"),
            GenericOptionModel("sandy_clay", "Sandy Clay"),
            GenericOptionModel("silty_clay", "Silty Clay"),
            GenericOptionModel("clay", "Clay")
        )
    }
}