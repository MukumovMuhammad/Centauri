package com.example.centauri.fragments.auth_frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.centauri.R
import com.example.centauri.databinding.FragmentSignInBinding
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel


class SignInFrag : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var  video : VideoView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Glide.with(view.context)
//            .load(R.drawable.stars_pattern)
//            .into(binding.imgStarBackground)

        video = binding.videoBg
        video.setVideoPath("android.resource://" + requireContext().packageName + "/" + R.raw.video_bg_ai_generated)
        video.start()
        binding.videoBg.setOnCompletionListener {
            video.start()
        }


        authViewModel.authState.observe(viewLifecycleOwner){
            when(it){
                is AuthState.Authenticated -> {
                    findNavController().navigate(R.id.action_signInFrag_to_mainActivity)
                }
                is AuthState.Error -> {
                    binding.tvError.text = it.message
                }

                else -> {

                }
            }
        }



        binding.btnSignIn.setOnClickListener{
            authViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFrag_to_signUpFrag)
        }
    }

    override fun onResume() {
        super.onResume()
        video.start()
    }

}