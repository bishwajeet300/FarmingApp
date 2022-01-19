package com.farmingapp.view.systemwaterdetails
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentSystemWaterSourceDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemWaterSourceDetailsFragment : Fragment() {

    private var _binding: FragmentSystemWaterSourceDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSystemWaterSourceDetailsBinding.inflate(inflater, container, false)
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