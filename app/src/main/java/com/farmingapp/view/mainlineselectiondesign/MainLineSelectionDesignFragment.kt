package com.farmingapp.view.mainlineselectiondesign
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmingapp.databinding.FragmentMainLineSelectionDesignBinding
import com.farmingapp.model.ResultSavedStatusModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainLineSelectionDesignFragment : Fragment() {

    private var _binding: FragmentMainLineSelectionDesignBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainLineSelectionDesignViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainLineSelectionDesignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    private fun setupClickListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}