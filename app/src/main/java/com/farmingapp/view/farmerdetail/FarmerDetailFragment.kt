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
import androidx.navigation.fragment.navArgs
import com.farmingapp.databinding.FragmentFarmerDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FarmerDetailFragment: Fragment() {

    private var _binding: FragmentFarmerDetailBinding? = null
    private val binding get() = _binding!!
    private val farmerDetailViewModel: FarmerDetailViewModel by viewModels()
    private val args: FarmerDetailFragmentArgs by navArgs()
    private lateinit var bottomSheetResultDialog: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }

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