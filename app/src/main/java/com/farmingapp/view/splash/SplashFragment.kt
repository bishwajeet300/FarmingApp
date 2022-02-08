package com.farmingapp.view.splash
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farmingapp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var otpTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setupNavigation() {
        try {
            otpTimer = object : CountDownTimer(3000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    val action = SplashFragmentDirections.actionSplashFragmentToLandingFragment()
                    findNavController().navigate(action)
                }
            }.start()
        } catch (e: Exception) {
            print(e.message)
        }
    }

    override fun onResume() {
        super.onResume()
        setupNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}