package com.farmingapp.view.terracedetails
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentTerraceDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TerraceDetailsFragment : Fragment() {

    private var _binding: FragmentTerraceDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTerraceDetailsBinding.inflate(inflater, container, false)
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