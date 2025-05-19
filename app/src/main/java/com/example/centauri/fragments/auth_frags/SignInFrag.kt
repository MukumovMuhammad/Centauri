package com.example.centauri.fragments.auth_frags

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

        binding.darkOverlay.visibility = View.GONE
        binding.loading.visibility = View.GONE

//        Video Settings
        video = binding.videoBg
        video.setVideoPath("android.resource://" + requireContext().packageName + "/" + R.raw.video_bg_ai_generated)

        // Adjust size to fill the
        video.setOnPreparedListener { mp ->
            mp.isLooping = true
        }
        video.start()


        binding.icClose.setOnClickListener {
            findNavController().navigate(R.id.action_signInFrag_to_mainActivity)
        }


        authViewModel.authState.observe(viewLifecycleOwner){
            when(it){
                is AuthState.Authenticated -> {
                    findNavController().navigate(R.id.action_signInFrag_to_mainActivity)
                }
                is AuthState.Error -> {
                    binding.darkOverlay.visibility = View.GONE
                    binding.loading.visibility = View.GONE

                    binding.signCard.visibility = View.VISIBLE
                    binding.icClose.visibility = View.VISIBLE
                    binding.tvError.text = it.message
                }
                is AuthState.Loading -> {

                    binding.darkOverlay.visibility = View.VISIBLE
                    binding.loading.visibility = View.VISIBLE

                    binding.signCard.visibility = View.GONE
                    binding.icClose.visibility = View.GONE

                    binding.tvError.text = ""

                }

                else -> {

                }
            }
        }



        binding.btnSignIn.setOnClickListener{
            binding.darkOverlay.visibility = View.VISIBLE
            binding.loading.visibility = View.VISIBLE
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