package com.farmingapp.view.farmerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farmingapp.databinding.FragmentFarmerDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FarmerDetailFragment: Fragment() {

    private var _binding: FragmentFarmerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerDetailBinding.inflate(inflater, container, false)
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