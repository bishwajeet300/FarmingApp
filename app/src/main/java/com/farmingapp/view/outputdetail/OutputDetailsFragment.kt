package com.farmingapp.view.outputdetail
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentOutputDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OutputDetailsFragment : Fragment() {

    private var _binding: FragmentOutputDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutputDetailsBinding.inflate(inflater, container, false)
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