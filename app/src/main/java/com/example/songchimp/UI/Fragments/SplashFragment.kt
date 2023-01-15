package com.example.songchimp.UI.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.songchimp.R
import com.example.songchimp.databinding.FragmentSplashScreenBinding

class SplashFragment : Fragment(R.layout.fragment_splash_screen) {
    private lateinit var binding: FragmentSplashScreenBinding

    private var fragCheck: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (fragCheck == 0){
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment2)
            }, 3000)
//            fragCheck = 1
//        } else if (fragCheck == 1){
//            requireActivity().finish()
//        }
    }
}