package com.farmingapp.view.plainfielddipperselection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmingapp.databinding.FragmentPlainFieldDipperWaterCalculationBinding
import com.farmingapp.model.ResultSavedStatusModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlainFieldDipperWaterCalculationFragment : Fragment() {

    private var _binding: FragmentPlainFieldDipperWaterCalculationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlainFieldDipperWaterCalculationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlainFieldDipperWaterCalculationBinding.inflate(inflater, container, false)
        val view = binding.root

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultSavedStatus.collect { value ->
                    when (value) {
                        is ResultSavedStatusModel.Failure -> TODO()
                        ResultSavedStatusModel.Pending -> TODO()
                        ResultSavedStatusModel.Saved -> TODO()
                    }
                }
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