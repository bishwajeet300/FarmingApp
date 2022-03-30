package com.farmingapp.view.sharedetail
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.farmingapp.R
import com.farmingapp.databinding.FragmentShareDetailsBinding
import com.farmingapp.model.OutputDetailsResultModel
import com.farmingapp.model.ResultFetchStatusModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShareDetailsFragment : Fragment() {

    private var _binding: FragmentShareDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShareDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultFetchStatus.collect { value ->
                    when (value) {
                        is ResultFetchStatusModel.Failure -> {
                            Snackbar.make(binding.divider, resources.getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show()
                        }
                        ResultFetchStatusModel.Pending -> {

                        }
                        is ResultFetchStatusModel.Success -> {
                            composeEmail(
                                arrayOf(binding.etAddress.text.toString()),
                                getFormattedText(value.data)
                            )
                        }
                    }
                }
            }
        }

        setupClickListener()
    }

    private fun getFormattedText(data: OutputDetailsResultModel): String {
        return StringBuffer()
            .append(getString(R.string.crop_name_hint)).append(" : ").append(data.cropName).append("\n")
            .append(getString(R.string.soil_type_hint)).append(" : ").append(data.soilType).append("\n")
            .append(getString(R.string.plant_to_plant_hint)).append(" : ").append(data.plantDistance).append("\n")
            .append(getString(R.string.row_to_row_hint)).append(" : ").append(data.rowDistance).append("\n")
            .append(getString(R.string.selected_dripper_size_hint)).append(" : ").append(data.dripperSize).append("\n")
            .append(getString(R.string.number_of_dripper_per_plant_hint)).append(" : ").append(data.dripperPerPlant).append("\n")
            .append(getString(R.string.selected_lateral_diameter_hint)).append(" : ").append(data.lateralDiameter).append("\n")
            .append(getString(R.string.length_of_lateral_hint)).append(" : ").append(data.lateralLength).append("\n")
            .append(getString(R.string.selected_mainline_diameter_hint)).append(" : ").append(data.mainlineDiameter).append("\n")
            .append(getString(R.string.mainline_length_hint)).append(" : ").append(data.mainlineLength).append("\n")
            .append(getString(R.string.number_of_lateral_submain_hint)).append(" : ").append(data.numberOfLateralSubMain).append("\n")
            .append(getString(R.string.number_of_dripper_submain_hint)).append(" : ").append(data.numberOfDripperForSubMain).append("\n")
            .append(getString(R.string.selected_submain_diameter_hint)).append(" : ").append(data.subMainDiameter).append("\n")
            .append(getString(R.string.length_of_submain_hint)).append(" : ").append(data.subMainLength)
            .toString()
    }

    private fun setupClickListener() {
        binding.btnFinish.setOnClickListener {
            viewModel.receiveUserAction()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun composeEmail(addresses: Array<String>, emailText: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, "Decision Support System for Design & Planning of Drip Irrigation System for Hilly Regions")
            putExtra(Intent.EXTRA_TEXT, emailText)
        }
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(requireContext(), R.string.application_not_found, Toast.LENGTH_LONG).show()
        }

        val action = ShareDetailsFragmentDirections.actionShareDetailsFragmentToLandingFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}