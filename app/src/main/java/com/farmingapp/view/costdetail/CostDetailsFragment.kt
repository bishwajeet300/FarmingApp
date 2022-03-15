package com.farmingapp.view.costdetail
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
import com.farmingapp.databinding.BottomsheetCostEditingBinding
import com.farmingapp.databinding.BottomsheetResultBinding
import com.farmingapp.databinding.FragmentCostDetailsBinding
import com.farmingapp.model.CostDetailAction
import com.farmingapp.model.CostModel
import com.farmingapp.model.GenericResultModel
import com.farmingapp.model.ResultCostSavedStatusModel
import com.farmingapp.view.helper.ResultAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CostDetailsFragment : Fragment(), OnEditClickListener {

    private var _binding: FragmentCostDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CostDetailsViewModel by viewModels()
    private lateinit var bottomSheetCostEditDialog: BottomSheetDialog
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var costResultAdapter: CostResultAdapter
    private lateinit var resultAdapter: ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultCostSavedStatus.collect { value ->
                    when (value) {
                        is ResultCostSavedStatusModel.Failure -> {
                            enableViews()
                            Snackbar.make(binding.divider, resources.getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show()
                        }
                        ResultCostSavedStatusModel.Pending -> {
                            enableViews()
                        }
                        is ResultCostSavedStatusModel.Saved -> {
                            disableViews()
                            setupResultBottomSheet(value.resultList)
                        }
                        is ResultCostSavedStatusModel.InitialState -> {
                            enableViews()
                            setupInitialScreen(value.dataList)
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
            viewModel.receiveUserAction(CostDetailAction.Submit)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnReset.setOnClickListener {
            resetViews()
        }
    }

    private fun resetViews() {
        enableViews()
    }

    private fun setupInitialScreen(initialState: List<CostModel>) {
        costResultAdapter = CostResultAdapter(initialState, this)
        binding.rvCostResult.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvCostResult.adapter = costResultAdapter
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
                bottomSheetResultDialog.setCancelable(true)
                bottomSheetResultDialog.dismiss()
                enableViews()
            }

            val action = CostDetailsFragmentDirections.actionCostDetailsFragment2ToShareDetailsFragment()
            findNavController().navigate(action)
        }

        if (bottomSheetResultDialog.isShowing.not()) {
            bottomSheetResultDialog.show()
        }
    }

    override fun onEditClick(model: CostModel) {
        bottomSheetCostEditDialog = BottomSheetDialog(requireContext())
        val costBottomSheetBinding = BottomsheetCostEditingBinding.inflate(layoutInflater, null, false)
        bottomSheetCostEditDialog.setContentView(costBottomSheetBinding.root)

        costBottomSheetBinding.etQuantity.setText(model.quantity)
        costBottomSheetBinding.etAmount.setText(model.amount)

        costBottomSheetBinding.btnSubmit.setOnClickListener {
            viewModel.receiveUserAction(CostDetailAction.UpdateCostModel(
                model = CostModel(
                    id = model.id,
                    title = model.title,
                    value = model.value,
                    quantity = costBottomSheetBinding.etQuantity.text.toString(),
                    rate = model.rate,
                    amount = costBottomSheetBinding.etAmount.text.toString()
                )
            ))

            if (bottomSheetCostEditDialog.isShowing) {
                bottomSheetCostEditDialog.setCancelable(true)
                bottomSheetCostEditDialog.dismiss()
            }
        }

        if (bottomSheetCostEditDialog.isShowing.not()) {
            bottomSheetCostEditDialog.show()
        }
    }

    private fun disableViews() {
        binding.btnBack.isEnabled = false
        binding.btnReset.isEnabled = false
        binding.btnSubmit.isEnabled = false
        
    }

    private fun enableViews() {
        binding.btnBack.isEnabled = true
        binding.btnReset.isEnabled = true
        binding.btnSubmit.isEnabled = true
    
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}