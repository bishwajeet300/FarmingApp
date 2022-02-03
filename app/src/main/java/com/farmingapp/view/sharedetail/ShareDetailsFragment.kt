package com.farmingapp.view.sharedetail
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farmingapp.databinding.FragmentShareDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareDetailsFragment : Fragment() {

    private var _binding: FragmentShareDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareDetailsBinding.inflate(inflater, container, false)
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