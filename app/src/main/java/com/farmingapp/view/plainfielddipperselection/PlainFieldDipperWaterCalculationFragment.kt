package com.farmingapp.view.plainfielddipperselection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentPlainFieldDipperWaterCalculationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlainFieldDipperWaterCalculationFragment : Fragment() {

    private var _binding: FragmentPlainFieldDipperWaterCalculationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlainFieldDipperWaterCalculationBinding.inflate(inflater, container, false)
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