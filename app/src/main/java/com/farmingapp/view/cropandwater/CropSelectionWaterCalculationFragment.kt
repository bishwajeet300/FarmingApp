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
import com.farmingapp.view.helper.OnNextListener
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
    private lateinit var bottomSheetEPanDialog: BottomSheetDialog
    private lateinit var bottomSheetSoilDialog: BottomSheetDialog
    private lateinit var resultAdapter: ResultAdapter
    private lateinit var cropOptionsAdapter: OptionsAdapter
    private lateinit var soilOptionsAdapter: OptionsAdapter
    private lateinit var ePanOptionsAdapter: OptionsAdapter


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
        setupEPanOptions()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupEPanOptions() {
        bottomSheetEPanDialog = BottomSheetDialog(requireContext())
        val ePanBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetEPanDialog.setContentView(ePanBottomSheetBinding.root)

        ePanOptionsAdapter = OptionsAdapter(OptionsType.EPAN, getEPanList(), this)
        ePanBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        ePanBottomSheetBinding.rvOptions.adapter = ePanOptionsAdapter

        binding.etEPan.setOnTouchListener { v, _ ->
            bottomSheetEPanDialog.show()
            v.performClick()
        }
    }

    private fun setupResultBottomSheet(resultList: List<GenericResultModel>, isTerraceField: Boolean) {
        bottomSheetResultDialog = BottomSheetDialog(requireContext())
        val resultBottomSheetBinding = BottomsheetResultBinding.inflate(layoutInflater, null, false)
        bottomSheetResultDialog.setContentView(resultBottomSheetBinding.root)

        resultAdapter = ResultAdapter(resultList, object : OnNextListener {
            override fun next() {
                if (bottomSheetResultDialog.isShowing) {
                    bottomSheetResultDialog.setCancelable(true)
                    bottomSheetResultDialog.dismiss()
                    enableViews()
                }
                if (isTerraceField) {
                    val action = CropSelectionWaterCalculationFragmentDirections.actionCropSelectionWaterCalculationFragmentToTerraceDetailsFragment()
                    findNavController().navigate(action)
                } else {
                    val action = CropSelectionWaterCalculationFragmentDirections.actionCropSelectionWaterCalculationFragmentToPlainFieldDipperWaterCalculationFragment()
                    findNavController().navigate(action)
                }
            }
        })
        resultBottomSheetBinding.rvResult.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        resultBottomSheetBinding.rvResult.adapter = resultAdapter

        if (bottomSheetResultDialog.isShowing.not()) {
            bottomSheetResultDialog.show()
        }
    }

    private fun setupClickListener() {
        binding.btnSubmit.setOnClickListener {
            if (isFormValidated()) {
                disableViews()
                viewModel.receiveUserAction(
                    CropSelectionWaterCalculationAction.Submit(
                        CropSelectionWaterCalculationUserModel(
                            plantDistance = binding.etPlantToPlantDistance.text.toString(),
                            rowDistance = binding.etRowToRowDistance.text.toString(),
                            plantShadowArea = binding.etPlantShadowArea.text.toString(),
                            wettedArea = binding.etWettedArea.text.toString()
                        )
                    )
                )
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnReset.setOnClickListener {
            resetViews()
        }
    }

    override fun onOptionsClick(type: OptionsType, model: GenericOptionModel) {
        if (type == OptionsType.SOIL) {
            binding.etSoilType.setText(model.label)
            if (bottomSheetSoilDialog.isShowing) {
                bottomSheetSoilDialog.dismiss()
            }
        }

        if (type == OptionsType.CROP) {
            binding.etCropName.setText(model.label)
            if (bottomSheetCropDialog.isShowing) {
                bottomSheetCropDialog.dismiss()
            }
        }

        if (type == OptionsType.EPAN) {
            binding.etEPan.setText(model.label)
            if (bottomSheetEPanDialog.isShowing) {
                bottomSheetEPanDialog.dismiss()
            }
        }

        viewModel.receiveUserAction(
            CropSelectionWaterCalculationAction.SaveOption(
                model, type
            )
        )
    }

    private fun resetViews() {
        enableViews()
        binding.etCropName.setText("")
        binding.etSoilType.setText("")
        binding.etEPan.setText("")
        binding.etPlantShadowArea.setText("")
        binding.etPlantToPlantDistance.setText("")
        binding.etRowToRowDistance.setText("")
        binding.etWettedArea.setText("")
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = true
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

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etCropName.text.isNullOrEmpty()) {
            binding.etCropName.error = "*Required"
            isValid = false
        } else {
            binding.etCropName.error = null
        }

        if (binding.etSoilType.text.isNullOrEmpty()) {
            binding.etSoilType.error = "*Required"
            isValid = false
        } else {
            binding.etSoilType.error = null
        }

        if (binding.etEPan.text.isNullOrEmpty()) {
            binding.etEPan.error = "*Required"
            isValid = false
        } else {
            binding.etEPan.error = null
        }

        if (binding.etPlantShadowArea.text.isNullOrEmpty()) {
            binding.etPlantShadowArea.error = "*Required"
            isValid = false
        } else {
            binding.etPlantShadowArea.error = null
        }

        if (binding.etPlantToPlantDistance.text.isNullOrEmpty()) {
            binding.etPlantToPlantDistance.error = "*Required"
            isValid = false
        } else {
            binding.etPlantToPlantDistance.error = null
        }

        if (binding.etRowToRowDistance.text.isNullOrEmpty()) {
            binding.etRowToRowDistance.error = "*Required"
            isValid = false
        } else {
            binding.etRowToRowDistance.error = null
        }

        if (binding.etWettedArea.text.isNullOrEmpty()) {
            binding.etWettedArea.error = "*Required"
            isValid = false
        } else {
            binding.etWettedArea.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCropList(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "bean", label = "Bean"),
            GenericOptionModel(key = "broccoli", label = "Broccoli"),
            GenericOptionModel(key = "cabbage", label = "Cabbage"),
            GenericOptionModel(key = "carrot", label = "Carrot"),
            GenericOptionModel(key = "cauliflower", label = "Cauliflower"),
            GenericOptionModel(key = "cotton", label = "Cotton"),
            GenericOptionModel(key = "cucumber", label = "Cucumber"),
            GenericOptionModel(key = "groundnut", label = "Groundnut"),
            GenericOptionModel(key = "lentil", label = "Lentil"),
            GenericOptionModel(key = "maize", label = "Maize"),
            GenericOptionModel(key = "millet", label = "Millet"),
            GenericOptionModel(key = "onion", label = "Onion"),
            GenericOptionModel(key = "peanut", label = "Peanut"),
            GenericOptionModel(key = "pepper", label = "Pepper"),
            GenericOptionModel(key = "potato", label = "Potato"),
            GenericOptionModel(key = "pulses", label = "Pulses"),
            GenericOptionModel(key = "radish", label = "Radish"),
            GenericOptionModel(key = "sorghum", label = "Sorghum"),
            GenericOptionModel(key = "soyabean", label = "Soyabean"),
            GenericOptionModel(key = "spinach", label = "Spinach"),
            GenericOptionModel(key = "squash", label = "Squash"),
            GenericOptionModel(key = "sugarbeet", label = "Sugarbeet"),
            GenericOptionModel(key = "sunflower", label = "Sunflower"),
            GenericOptionModel(key = "tobacco", label = "Tobacco")
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

    private fun getEPanList(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel("1", "1"),
            GenericOptionModel("2", "2"),
            GenericOptionModel("3", "3"),
            GenericOptionModel("4", "4"),
            GenericOptionModel("5", "5"),
            GenericOptionModel("6", "6")
        )
    }
}