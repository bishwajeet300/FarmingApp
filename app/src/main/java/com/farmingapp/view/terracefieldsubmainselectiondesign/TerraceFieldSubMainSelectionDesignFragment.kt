package com.farmingapp.view.terracefieldsubmainselectiondesign
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
import com.farmingapp.databinding.FragmentTerraceFieldSubMainSelectionDesignBinding
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
class TerraceFieldSubMainSelectionDesignFragment : Fragment(), OnOptionsClickListener {

    private var _binding: FragmentTerraceFieldSubMainSelectionDesignBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TerraceFieldSubMainSelectionDesignViewModel by viewModels()
    private lateinit var bottomSheetSubMainDiameterDialog: BottomSheetDialog
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var subMainDiameterAdapter: OptionsAdapter
    private lateinit var resultAdapter: ResultAdapter

    private val subMainLengthString: StringBuffer = StringBuffer()
    private var eachSubMainLength: MutableList<Double> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentTerraceFieldSubMainSelectionDesignBinding.inflate(inflater, container, false)

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

        setupSubMainDiameterOptions()
        setupClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSubMainDiameterOptions() {
        bottomSheetSubMainDiameterDialog = BottomSheetDialog(requireContext())
        val subMainDiameterBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetSubMainDiameterDialog.setContentView(subMainDiameterBottomSheetBinding.root)

        subMainDiameterAdapter = OptionsAdapter(OptionsType.SUB_MAIN_DIAMETER, getSubMainDiameter(), this)
        subMainDiameterBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        subMainDiameterBottomSheetBinding.rvOptions.adapter = subMainDiameterAdapter

        binding.etSubMainDiameter.setOnTouchListener { v, _ ->
            bottomSheetSubMainDiameterDialog.show()
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

                val action = TerraceFieldSubMainSelectionDesignFragmentDirections.actionTerraceFieldSubMainSelectionDesignFragmentToMainLineSelectionDesignFragment()
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
        binding.btnAddSubMainTerraceLength.setOnClickListener {
            if (binding.etEachSubMainTerraceLength.text.isNullOrBlank().not()) {
                binding.etEachSubMainTerraceLength.error = null
                val entry = binding.etEachSubMainTerraceLength.text.toString()

                if (subMainLengthString.isEmpty()) {
                    subMainLengthString.append(entry)
                } else {
                    subMainLengthString.append("\n").append(entry)
                }
                binding.tvSubMainTerraceLengths.setLines(binding.tvSubMainTerraceLengths.lineCount + 1)
                binding.tvSubMainTerraceLengths.text = subMainLengthString
                eachSubMainLength.add(entry.toDouble())
                binding.etTotalSubMainLength.setText("${eachSubMainLength.sum()}")
                binding.etEachSubMainTerraceLength.setText("")
            } else {
                binding.etEachSubMainTerraceLength.error = resources.getString(R.string.value_missing)
            }

        }

        binding.btnSubmit.setOnClickListener {
            if (isFormValidated()) {
                disableViews()
                viewModel.receiveUserAction(
                    TerraceFieldSubMainSelectionDesignAction.Submit(
                        TerraceFieldSubMainSelectionDesignUserModel(
                            subMainLengthPerTerrace = eachSubMainLength
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
        if (type == OptionsType.SUB_MAIN_DIAMETER) {
            binding.etSubMainDiameter.setText(model.label)
            if (bottomSheetSubMainDiameterDialog.isShowing) {
                bottomSheetSubMainDiameterDialog.dismiss()
            }
        }

        viewModel.receiveUserAction(
            TerraceFieldSubMainSelectionDesignAction.SaveOption(
                model, type
            )
        )
    }

    private fun resetViews() {
        enableViews()
        binding.etSubMainDiameter.setText("")
        binding.etEachSubMainTerraceLength.setText("")
        binding.etTotalSubMainLength.setText("")
        binding.tvSubMainTerraceLengths.text = ""
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = true
        binding.btnAddSubMainTerraceLength.isEnabled = false
        binding.etSubMainDiameter.isEnabled = false
        binding.etEachSubMainTerraceLength.isEnabled = false
    }

    private fun enableViews() {
        binding.btnBack.isEnabled = true
        binding.btnReset.isEnabled = true
        binding.btnSubmit.isEnabled = true
        binding.btnAddSubMainTerraceLength.isEnabled = true
        binding.etSubMainDiameter.isEnabled = true
        binding.etEachSubMainTerraceLength.isEnabled = true
    }

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etSubMainDiameter.text.isNullOrEmpty()) {
            binding.etSubMainDiameter.error = "*Required"
            isValid = false
        } else {
            binding.etSubMainDiameter.error = null
        }

        if (binding.etTotalSubMainLength.text.isNullOrEmpty()) {
            binding.etTotalSubMainLength.error = "*Required"
            isValid = false
        } else {
            binding.etTotalSubMainLength.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSubMainDiameter(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "32", label = "32 mm"),
            GenericOptionModel(key = "40", label = "40 mm"),
            GenericOptionModel(key = "50", label = "50 mm"),
            GenericOptionModel(key = "63", label = "63 mm"),
            GenericOptionModel(key = "75", label = "75 mm")
        )
    }
}