package com.farmingapp.view.cropandwater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentCropSelectionWaterCalculationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropSelectionWaterCalculationFragment : Fragment() {

    private var _binding: FragmentCropSelectionWaterCalculationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCropSelectionWaterCalculationBinding.inflate(inflater, container, false)
        val view = binding.root

        setupClickListener()


        return view
    }

    private fun setupClickListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}