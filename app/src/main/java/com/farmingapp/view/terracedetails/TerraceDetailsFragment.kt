package com.farmingapp.view.terracedetails
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
import com.farmingapp.databinding.BottomsheetResultBinding
import com.farmingapp.databinding.FragmentTerraceDetailsBinding
import com.farmingapp.model.GenericResultModel
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.TerraceDetailUserModel
import com.farmingapp.model.UserAction
import com.farmingapp.view.helper.OnNextListener
import com.farmingapp.view.helper.ResultAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder

@AndroidEntryPoint
class TerraceDetailsFragment : Fragment() {

    private var _binding: FragmentTerraceDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TerraceDetailsViewModel by viewModels()
    private lateinit var bottomSheetResultDialog: BottomSheetDialog
    private lateinit var resultAdapter: ResultAdapter

    private val lengthString: StringBuilder = StringBuilder()
    private val widthString: StringBuilder = StringBuilder()
    private val heightString: StringBuilder = StringBuilder()
    private var eachLength: MutableList<Double> = mutableListOf()
    private var eachWidth: MutableList<Double> = mutableListOf()
    private var eachHeight: MutableList<Double> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTerraceDetailsBinding.inflate(inflater, container, false)
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

        setupClickListener()
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

                val action = TerraceDetailsFragmentDirections.actionTerraceDetailsFragmentToTerraceFieldLateralDetailsFragment()
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
        binding.btnAddTerraceLength.setOnClickListener {
            if (binding.etEachTerraceLength.text.isNullOrBlank().not()) {
                binding.etEachTerraceLength.error = null
                val entry = binding.etEachTerraceLength.text.toString()

                if (lengthString.isEmpty()) {
                    lengthString.append(entry)
                } else {
                    lengthString.append("\n").append(entry)
                }
                binding.tvTerraceLengths.setLines(binding.tvTerraceLengths.lineCount + 1)
                binding.tvTerraceLengths.text = lengthString
                eachLength.add(entry.toDouble())
                binding.etTotalTerraceLength.setText("${eachLength.sum()}")
                binding.etEachTerraceLength.setText("")
            } else {
                binding.etEachTerraceLength.error = resources.getString(R.string.value_missing)
            }
        }

        binding.btnAddTerraceWidth.setOnClickListener {
            if (binding.etEachTerraceWidth.text.isNullOrBlank().not()) {
                binding.etEachTerraceWidth.error = null
                val entry = binding.etEachTerraceWidth.text.toString()

                if (widthString.isEmpty()) {
                    widthString.append(entry)
                } else {
                    widthString.append("\n").append(entry)
                }
                binding.tvTerraceWidths.setLines(binding.tvTerraceWidths.lineCount + 1)
                binding.tvTerraceWidths.text = widthString
                eachWidth.add(entry.toDouble())
                binding.etTotalTerraceWidth.setText("${eachWidth.sum()}")
                binding.etEachTerraceWidth.setText("")
            } else {
                binding.etEachTerraceWidth.error = resources.getString(R.string.value_missing)
            }
        }

        binding.btnAddTerraceCumulativeHeight.setOnClickListener {
            if (binding.etEachTerraceCumulativeHeight.text.isNullOrBlank().not()) {
                binding.etEachTerraceCumulativeHeight.error = null
                val entry = binding.etEachTerraceCumulativeHeight.text.toString()

                if (heightString.isEmpty()) {
                    heightString.append(entry)
                } else {
                    heightString.append("\n").append(entry)
                }
                binding.tvTerraceCumulativeHeights.setLines(binding.tvTerraceCumulativeHeights.lineCount + 1)
                binding.tvTerraceCumulativeHeights.text = heightString
                eachHeight.add(entry.toDouble())
                binding.etTotalTerraceCumulativeHeight.setText("${eachHeight.sum()}")
                binding.etEachTerraceCumulativeHeight.setText("")
            } else {
                binding.etEachTerraceCumulativeHeight.error = resources.getString(R.string.value_missing)
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (isFormValidated()) {
                viewModel.receiveUserAction(
                    UserAction.Submit(
                        TerraceDetailUserModel(
                            eachTerraceLength = eachLength, eachTerraceWidth = eachWidth, eachTerraceHeight = eachHeight
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

    private fun resetViews() {
        lengthString.clear()
        widthString.clear()
        heightString.clear()
        eachLength.clear()
        eachHeight.clear()
        eachWidth.clear()
        binding.etTotalTerraceLength.setText("")
        binding.etTotalTerraceWidth.setText("")
        binding.etTotalTerraceCumulativeHeight.setText("")
        binding.etEachTerraceLength.setText("")
        binding.etEachTerraceWidth.setText("")
        binding.etEachTerraceCumulativeHeight.setText("")
        binding.tvTerraceLengths.text = ""
        binding.tvTerraceWidths.text = ""
        binding.tvTerraceCumulativeHeights.text = ""
    }

    private fun isFormValidated(): Boolean {
        var isValid = true

        if (binding.etTotalTerraceCumulativeHeight.text.isNullOrEmpty()) {
            binding.etEachTerraceCumulativeHeight.error = "*Required"
            isValid = false
        } else {
            binding.etEachTerraceCumulativeHeight.error = null
        }

        if (binding.etTotalTerraceLength.text.isNullOrEmpty()) {
            binding.etEachTerraceLength.error = "*Required"
            isValid = false
        } else {
            binding.etEachTerraceLength.error = null
        }

        if (binding.etTotalTerraceWidth.text.isNullOrEmpty()) {
            binding.etEachTerraceWidth.error = "*Required"
            isValid = false
        } else {
            binding.etEachTerraceWidth.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}