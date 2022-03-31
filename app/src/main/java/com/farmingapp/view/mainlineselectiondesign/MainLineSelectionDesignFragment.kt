package com.farmingapp.view.mainlineselectiondesign
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
import com.farmingapp.databinding.FragmentMainLineSelectionDesignBinding
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
class MainLineSelectionDesignFragment : Fragment(), OnOptionsClickListener {

    private var _binding: FragmentMainLineSelectionDesignBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainLineSelectionDesignViewModel by viewModels()
    private lateinit var bottomSheetMainLineDiameterDialog: BottomSheetDialog
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var mainLineDiameterAdapter: OptionsAdapter
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainLineSelectionDesignBinding.inflate(inflater, container, false)
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

        setupMainLineDiameterOptions()
        setupClickListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupMainLineDiameterOptions() {
        bottomSheetMainLineDiameterDialog = BottomSheetDialog(requireContext())
        val mainLineDiameterBottomSheetBinding = BottomsheetOptionsBinding.inflate(layoutInflater, null, false)
        bottomSheetMainLineDiameterDialog.setContentView(mainLineDiameterBottomSheetBinding.root)

        mainLineDiameterAdapter = OptionsAdapter(OptionsType.MAIN_DIAMETER, getMainLineDiameter(), this)
        mainLineDiameterBottomSheetBinding.rvOptions.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mainLineDiameterBottomSheetBinding.rvOptions.adapter = mainLineDiameterAdapter

        binding.etMainlineDiameter.setOnTouchListener { v, _ ->
            bottomSheetMainLineDiameterDialog.show()
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

                val action = MainLineSelectionDesignFragmentDirections.actionMainLineSelectionDesignFragmentToSystemWaterSourceDetailsFragment()
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
                disableViews()
                viewModel.receiveUserAction(
                    MainLineSelectionDesignAction.Submit(
                        MainLineSelectionDesignUserModel(
                            mainlineLength = binding.etMainlineLength.text.toString()
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
        if (type == OptionsType.MAIN_DIAMETER) {
            binding.etMainlineDiameter.setText(model.label)
            if (bottomSheetMainLineDiameterDialog.isShowing) {
                bottomSheetMainLineDiameterDialog.dismiss()
            }
        }

        viewModel.receiveUserAction(
            MainLineSelectionDesignAction.SaveOption(
                model, type
            )
        )
    }

    private fun resetViews() {
        enableViews()
        binding.etMainlineDiameter.setText("")
        binding.etMainlineLength.setText("")
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = true
        binding.etMainlineDiameter.isEnabled = false
        binding.etMainlineLength.isEnabled = false
    }

    private fun enableViews() {
        binding.btnBack.isEnabled = true
        binding.btnReset.isEnabled = true
        binding.btnSubmit.isEnabled = true
        binding.etMainlineDiameter.isEnabled = true
        binding.etMainlineLength.isEnabled = true
    }

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etMainlineDiameter.text.isNullOrEmpty()) {
            binding.etMainlineDiameter.error = "*Required"
            isValid = false
        } else {
            binding.etMainlineDiameter.error = null
        }

        if (binding.etMainlineLength.text.isNullOrEmpty()) {
            binding.etMainlineLength.error = "*Required"
            isValid = false
        } else {
            binding.etMainlineLength.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMainLineDiameter(): List<GenericOptionModel> {
        return listOf(
            GenericOptionModel(key = "40", label = "40 mm"),
            GenericOptionModel(key = "50", label = "50 mm"),
            GenericOptionModel(key = "63", label = "63 mm"),
            GenericOptionModel(key = "75", label = "75 mm"),
            GenericOptionModel(key = "90", label = "90 mm"),
            GenericOptionModel(key = "110", label = "110 mm")
        )
    }
}