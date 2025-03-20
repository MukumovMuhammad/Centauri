package com.example.centauri.fragments.auth_frags

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.centauri.R
import com.example.centauri.databinding.FragmentWellcomeBinding
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel


class WellcomeFragment : Fragment() {

    private lateinit var binding : FragmentWellcomeBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWellcomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(view.context)
            .load(R.drawable.stars_pattern)
            .into(binding.imgStarBackground)

        super.onViewCreated(view, savedInstanceState)


        if (authViewModel.authState.value == AuthState.Authenticated){
            Log.i("WellcomeFragment_TAG", "onViewCreated: ${authViewModel.authState.value}")
            findNavController().navigate(R.id.action_wellcomeFragment_to_mainActivity)
            findNavController().popBackStack();
        }
        else{
            Log.i("WellcomeFragment_TAG", "onViewCreated: ${authViewModel.authState.value}")
        }

        binding.start.setOnClickListener {
            findNavController().navigate(R.id.action_wellcomeFragment_to_signUpFrag)
        }
    }


}