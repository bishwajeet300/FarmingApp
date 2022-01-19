package com.farmingapp.view.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        val view = binding.root

        setupClickListener()

        return view
    }

    private fun setupClickListener() {
        binding.cvTerraceField.setOnClickListener {
            val action =
                LandingFragmentDirections.actionLandingFragmentToFarmerDetailFragment(FieldDesign.TERRACE)
            findNavController().navigate(action)
        }

        binding.cvPlainField.setOnClickListener {
            val action =
                LandingFragmentDirections.actionLandingFragmentToFarmerDetailFragment(FieldDesign.PLAIN)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}