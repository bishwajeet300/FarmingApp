package com.farmingapp.view.plainfieldlateralselectiondesign
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
import com.farmingapp.databinding.FragmentPlainFieldLateralSelectionDesignBinding
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
class PlainFieldLateralSelectionDesignFragment : Fragment(), OnOptionsClickListener {

    private var _binding: FragmentPlainFieldLateralSelectionDesignBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlainFieldLateralSelectionDesignViewModel by viewModels()
    private lateinit var bottomSheetLateralDiameterDialog: BottomSheetDialog
    private lateinit var bottomSheetPipeMaterialDialog: BottomSheetDialog
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var lateralDiameterAdapter: OptionsAdapter
    private lateinit var pipeMaterialAdapter: OptionsAdapter
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentPlainFieldLateralSelectionDesignBinding.inflate(inflater, container, false)

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
                            setupResultBottomSheet(value.resultList)
                        }
                    }
                }
            }
        }

        setupLateralDiameterOptions()
        setupPipeMaterialOptions()
        setupClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupLateralDiameterOptions() {
        bottomSheetLateralDiameterDialog = BottomSheetDialog(requireContext())
        val lateralDiameterBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetLateralDiameterDialog.setContentView(lateralDiameterBottomSheetBinding.root)

        lateralDiameterAdapter = OptionsAdapter(OptionsType.LATERAL_DIAMETER, getLateralDiameter(), this)
        lateralDiameterBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        lateralDiameterBottomSheetBinding.rvOptions.adapter = lateralDiameterAdapter

        binding.etLateralDiameter.setOnTouchListener { v, _ ->
            bottomSheetLateralDiameterDialog.show()
            v.performClick()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPipeMaterialOptions() {
        bottomSheetPipeMaterialDialog = BottomSheetDialog(requireContext())
        val pipeMaterialBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetPipeMaterialDialog.setContentView(pipeMaterialBottomSheetBinding.root)

        pipeMaterialAdapter = OptionsAdapter(OptionsType.PIPE_MATERIAL, getPipeMaterial(), this)
        pipeMaterialBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        pipeMaterialBottomSheetBinding.rvOptions.adapter = pipeMaterialAdapter

        binding.etPipeMaterial.setOnTouchListener { v, _ ->
            bottomSheetPipeMaterialDialog.show()
            v.performClick()
        }
    }

    private fun setupResultBottomSheet(resultList: List<GenericResultModel>) {
        bottomSheetResultDialog = BottomSheetDialog(requireContext())
        val resultBottomSheetBinding = BottomsheetResultBinding.inflate(layoutInflater, null, false)
        bottomSheetResultDialog.setContentView(resultBottomSheetBinding.root)

        resultAdapter = ResultAdapter(resultList)
        resultBottomSheetBinding.rvResult.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        resultBottomSheetBinding.rvResult.adapter = resultAdapter

        resultBottomSheetBinding.btnNext.setOnClickListener {
            if (bottomSheetResultDialog.isShowing) {
                bottomSheetResultDialog.dismissWithAnimation
            }

            val action = PlainFieldLateralSelectionDesignFragmentDirections.actionPlainFieldLateralSelectionDesignFragmentToPlainFieldSubMainSelectionDesignFragment()
            findNavController().navigate(action)
        }

        if (bottomSheetResultDialog.isShowing.not()) {
            bottomSheetResultDialog.show()
        }
    }

    private fun setupClickListener() {
        binding.btnSubmit.setOnClickListener {
            if (isFormValidated()) {
                disableViews()
                viewModel.receiveUserAction(
                    PlainFieldLateralSelectionDesignAction.Submit(
                        PlainFieldLateralSelectionDesignUserModel(
                            lateralLengthSubMain = binding.etLateralLengthSubMain.text.toString()
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
        if (type == OptionsType.LATERAL_DIAMETER) {
            binding.etLateralDiameter.setText(model.label)
            if (bottomSheetLateralDiameterDialog.isShowing) {
                bottomSheetLateralDiameterDialog.dismiss()
            }
        }

        if (type == OptionsType.PIPE_MATERIAL) {
            binding.etPipeMaterial.setText(model.label)
            if (bottomSheetPipeMaterialDialog.isShowing) {
                bottomSheetPipeMaterialDialog.dismiss()
            }
        }

        viewModel.receiveUserAction(
            PlainFieldLateralSelectionDesignAction.SaveOption(
                model, type
            )
        )
    }

    private fun resetViews() {
        enableViews()
        binding.etLateralDiameter.setText("")
        binding.etPipeMaterial.setText("")
        binding.etLateralLengthSubMain.setText("")
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = false
        binding.etLateralDiameter.isEnabled = false
        binding.etPipeMaterial.isEnabled = false
        binding.etLateralLengthSubMain.isEnabled = false
    }

    private fun enableViews() {
        binding.btnBack.isEnabled = true
        binding.btnReset.isEnabled = true
        binding.btnSubmit.isEnabled = true
        binding.etLateralDiameter.isEnabled = true
        binding.etPipeMaterial.isEnabled = true
        binding.etLateralLengthSubMain.isEnabled = true
    }

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etLateralDiameter.text.isNullOrEmpty()) {
            binding.etLateralDiameter.error = "*Required"
            isValid = false
        } else {
            binding.etLateralDiameter.error = null
        }

        if (binding.etPipeMaterial.text.isNullOrEmpty()) {
            binding.etPipeMaterial.error = "*Required"
            isValid = false
        } else {
            binding.etPipeMaterial.error = null
        }

        if (binding.etLateralLengthSubMain.text.isNullOrEmpty()) {
            binding.etLateralLengthSubMain.error = "*Required"
            isValid = false
        } else {
            binding.etLateralLengthSubMain.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLateralDiameter(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "12", label = "12 mm"),
            GenericOptionModel(key = "16", label = "16 mm"),
            GenericOptionModel(key = "20", label = "20 mm")
        )
    }

    private fun getPipeMaterial(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "aluminium", label = "Aluminium"),
            GenericOptionModel(key = "brass", label = "Brass"),
            GenericOptionModel(key = "cast_iron", label = "Cast Iron"),
            GenericOptionModel(key = "concrete", label = "Concrete"),
            GenericOptionModel(key = "galvanised_iron", label = "Galvanised Iron"),
            GenericOptionModel(key = "pvc", label = "PVC"),
            GenericOptionModel(key = "smooth_pipes", label = "Smooth Pipes"),
            GenericOptionModel(key = "steel", label = "Steel"),
            GenericOptionModel(key = "wrought_iron", label = "Wrought Iron")
        )
    }
}