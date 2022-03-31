package com.farmingapp.view.plainfielddipperselection
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
import com.farmingapp.databinding.FragmentPlainFieldDipperWaterCalculationBinding
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
class PlainFieldDipperWaterCalculationFragment : Fragment(), OnOptionsClickListener {

    private var _binding: FragmentPlainFieldDipperWaterCalculationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlainFieldDipperWaterCalculationViewModel by viewModels()
    private lateinit var bottomSheetDripperDialog: BottomSheetDialog
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var dripperOptionsAdapter: OptionsAdapter
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentPlainFieldDipperWaterCalculationBinding.inflate(inflater, container, false)

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

        setupDripperOptions()
        setupClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDripperOptions() {
        bottomSheetDripperDialog = BottomSheetDialog(requireContext())
        val dripperBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetDripperDialog.setContentView(dripperBottomSheetBinding.root)

        dripperOptionsAdapter = OptionsAdapter(OptionsType.DRIPPER, getDripperList(), this)
        dripperBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        dripperBottomSheetBinding.rvOptions.adapter = dripperOptionsAdapter

        binding.etDripper.setOnTouchListener { v, _ ->
            bottomSheetDripperDialog.show()
            v.performClick()
        }
    }

    private fun setupResultBottomSheet(resultList: List<GenericResultModel>) {
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

                val action = PlainFieldDipperWaterCalculationFragmentDirections.actionPlainFieldDipperWaterCalculationFragmentToPlainFieldLateralSelectionDesignFragment()
                findNavController().navigate(action)
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
                viewModel.receiveUserAction(
                    PlainFieldDipperWaterCalculationAction.Submit(
                        PlainFieldDipperWaterCalculationUserModel(
                            dripperSpacing = binding.etDripperSpacing.text.toString(),
                            lateralSpacing = binding.etLateralSpacing.text.toString(),
                            dripperPerPlant = binding.etDripperPerPlant.text.toString(),
                            fieldLength = binding.etFieldLength.text.toString(),
                            fieldWidth = binding.etFieldWidth.text.toString()
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
        if (type == OptionsType.DRIPPER) {
            binding.etDripper.setText(model.label)
            if (bottomSheetDripperDialog.isShowing) {
                bottomSheetDripperDialog.dismiss()
            }
        }

        viewModel.receiveUserAction(
            PlainFieldDipperWaterCalculationAction.SaveOption(
                model, type
            )
        )
    }

    private fun resetViews() {
        enableViews()
        binding.etDripper.setText("")
        binding.etDripperSpacing.setText("")
        binding.etLateralSpacing.setText("")
        binding.etDripperPerPlant.setText("1")
        binding.etFieldLength.setText("")
        binding.etFieldWidth.setText("")
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = true
        binding.etDripper.isEnabled = false
        binding.etDripperSpacing.isEnabled = false
        binding.etLateralSpacing.isEnabled = false
        binding.etDripperPerPlant.isEnabled = false
        binding.etFieldLength.isEnabled = false
        binding.etFieldWidth.isEnabled = false
    }

    private fun enableViews() {
        binding.btnBack.isEnabled = true
        binding.btnReset.isEnabled = true
        binding.btnSubmit.isEnabled = true
        binding.etDripper.isEnabled = true
        binding.etDripperSpacing.isEnabled = true
        binding.etLateralSpacing.isEnabled = true
        binding.etDripperPerPlant.isEnabled = true
        binding.etFieldLength.isEnabled = true
        binding.etFieldWidth.isEnabled = true
    }

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etDripper.text.isNullOrEmpty()) {
            binding.etDripper.error = "*Required"
            isValid = false
        } else {
            binding.etDripper.error = null
        }

        if (binding.etDripperSpacing.text.isNullOrEmpty()) {
            binding.etDripperSpacing.error = "*Required"
            isValid = false
        } else {
            binding.etDripperSpacing.error = null
        }

        if (binding.etLateralSpacing.text.isNullOrEmpty()) {
            binding.etLateralSpacing.error = "*Required"
            isValid = false
        } else {
            binding.etLateralSpacing.error = null
        }

        if (binding.etDripperPerPlant.text.isNullOrEmpty()) {
            binding.etDripperPerPlant.error = "*Required"
            isValid = false
        } else {
            binding.etDripperPerPlant.error = null
        }

        if (binding.etFieldLength.text.isNullOrEmpty()) {
            binding.etFieldLength.error = "*Required"
            isValid = false
        } else {
            binding.etFieldLength.error = null
        }

        if (binding.etFieldWidth.text.isNullOrEmpty()) {
            binding.etFieldWidth.error = "*Required"
            isValid = false
        } else {
            binding.etFieldWidth.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDripperList(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel("2", "2 lph"),
            GenericOptionModel("4", "4 lph")
        )
    }
}