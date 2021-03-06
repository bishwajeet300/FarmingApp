package com.farmingapp.view.systemwaterdetails
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
import com.farmingapp.databinding.FragmentSystemWaterSourceDetailsBinding
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
class SystemWaterSourceDetailsFragment : Fragment(), OnOptionsClickListener {

    private var _binding: FragmentSystemWaterSourceDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SystemWaterSourceDetailsViewModel by viewModels()
    private lateinit var bottomSheetWaterSourceDialog: BottomSheetDialog
    private lateinit var bottomSheetWaterTankLocationDialog: BottomSheetDialog
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var waterSourceAdapter: OptionsAdapter
    private lateinit var waterTankLocationAdapter: OptionsAdapter
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSystemWaterSourceDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultSavedStatus.collect { value ->
                    when (value) {
                        is ResultSavedStatusModel.Failure -> {
                            Snackbar.make(binding.divider, resources.getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show()
                        }
                        ResultSavedStatusModel.Pending -> {
                        }
                        is ResultSavedStatusModel.Saved -> {
                            setupResultBottomSheet(value.resultList)
                        }
                    }
                }
            }
        }

        setupWaterSourceOptions()
        setupWaterTankLocationOptions()
        setupClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupWaterSourceOptions() {
        bottomSheetWaterSourceDialog = BottomSheetDialog(requireContext())
        val waterSourceBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetWaterSourceDialog.setContentView(waterSourceBottomSheetBinding.root)

        waterSourceAdapter = OptionsAdapter(OptionsType.WATER_SOURCE, getWaterSources(), this)
        waterSourceBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        waterSourceBottomSheetBinding.rvOptions.adapter = waterSourceAdapter

        binding.etWaterSource.setOnTouchListener { v, _ ->
            bottomSheetWaterSourceDialog.show()
            v.performClick()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupWaterTankLocationOptions() {
        bottomSheetWaterTankLocationDialog = BottomSheetDialog(requireContext())
        val waterTankLocationBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetWaterTankLocationDialog.setContentView(waterTankLocationBottomSheetBinding.root)

        waterTankLocationAdapter = OptionsAdapter(OptionsType.WATER_TANK_LOCATION, getWaterTankLocation(), this)
        waterTankLocationBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        waterTankLocationBottomSheetBinding.rvOptions.adapter = waterTankLocationAdapter

        binding.etWaterTankLocation.setOnTouchListener { v, _ ->
            bottomSheetWaterTankLocationDialog.show()
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
                }

                val action = SystemWaterSourceDetailsFragmentDirections.actionSystemWaterSourceDetailsFragmentToOutputDetailsFragment()
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
                    SystemWaterSourceDetailsAction.Submit
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
        if (type == OptionsType.WATER_SOURCE) {
            binding.etWaterSource.setText(model.label)
            if (bottomSheetWaterSourceDialog.isShowing) {
                bottomSheetWaterSourceDialog.dismiss()
            }
        }

        if (type == OptionsType.WATER_TANK_LOCATION) {
            binding.etWaterTankLocation.setText(model.label)
            if (bottomSheetWaterTankLocationDialog.isShowing) {
                bottomSheetWaterTankLocationDialog.dismiss()
            }
        }

        viewModel.receiveUserAction(
            SystemWaterSourceDetailsAction.SaveOption(
                model, type
            )
        )
    }

    private fun resetViews() {
        binding.etWaterSource.setText("")
        binding.etWaterTankLocation.setText("")
    }

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etWaterSource.text.isNullOrEmpty()) {
            binding.etWaterSource.error = "*Required"
            isValid = false
        } else {
            binding.etWaterSource.error = null
        }

        if (binding.etWaterTankLocation.text.isNullOrEmpty()) {
            binding.etWaterTankLocation.error = "*Required"
            isValid = false
        } else {
            binding.etWaterTankLocation.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getWaterSources(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "spring", label = "Spring"),
            GenericOptionModel(key = "river_nala", label = "River/Nala"),
            GenericOptionModel(key = "tube_well", label = "Tube Well"),
            GenericOptionModel(key = "pond_lake", label = "Pond/Lake"),
            GenericOptionModel(key = "open_well", label = "Open Well")
        )
    }

    private fun getWaterTankLocation(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "center", label = "Center"),
            GenericOptionModel(key = "corner", label = "Corner")
        )
    }
}