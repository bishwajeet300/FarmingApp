package com.farmingapp.view.farmerdetail

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
import androidx.navigation.fragment.navArgs
import com.farmingapp.R
import com.farmingapp.databinding.FragmentFarmerDetailBinding
import com.farmingapp.model.FarmerDetailUserModel
import com.farmingapp.model.ResultSavedStatusModel
import com.farmingapp.model.UserAction
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FarmerDetailFragment: Fragment() {

    private var _binding: FragmentFarmerDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FarmerDetailViewModel by viewModels()
    private val args: FarmerDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerDetailBinding.inflate(inflater, container, false)

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
                            Snackbar.make(binding.divider, resources.getString(R.string.user_saved), Snackbar.LENGTH_SHORT).show()
                            val action =
                                FarmerDetailFragmentDirections.actionFarmerDetailFragmentToCropSelectionWaterCalculationFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnSubmit.setOnClickListener {
            viewModel.receiveUserAction(
                UserAction.Submit(
                    FarmerDetailUserModel(
                        name = binding.etFullName.text.toString(),
                        phone = binding.etPhone.text.toString(),
                        email = binding.etEmail.text.toString(),
                        address = binding.etAddress.text.toString(),
                        field = args.fieldType.name
                    )
                )
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnReset.setOnClickListener {
            resetViews()
        }
    }

    private fun resetViews() {
        binding.etFullName.setText("")
        binding.etPhone.setText("")
        binding.etEmail.setText("")
        binding.etAddress.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}