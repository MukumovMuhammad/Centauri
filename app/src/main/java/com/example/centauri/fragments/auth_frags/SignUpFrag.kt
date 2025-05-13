package com.example.centauri.fragments.auth_frags

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.centauri.R
import com.example.centauri.databinding.FragmentSignUpBinding
import com.example.centauri.models.UserData
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SignUpFrag : Fragment() {

    companion object{
        val TAG = "SignUpFrag";
    }
    private lateinit var binding : FragmentSignUpBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var  video : VideoView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

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
                is AuthState.Authenticated-> {
                    findNavController().navigate(R.id.action_signUpFrag_to_mainActivity)
                }
                is AuthState.Error -> {
                    binding.tvError.text = it.message
                }

                else -> {

                }
            }
        }

        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFrag_to_signInFrag)
        }


        binding.btnSignUp.setOnClickListener{
            Log.i(TAG, "btnSignUp clicked")
            Log.i(TAG, "the entered password: ${binding.etPassword.text.toString()}")
            if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
                Log.i(TAG, "passwords do not match")
                binding.tvError.text = "Passwords do not match"

                lifecycleScope.launch {
                    Log.i(TAG, "delay started")
                    delay(2000);
                    binding.tvError.text = ""
                }
                Log.i(TAG, "Returning from btnSignUp")
                return@setOnClickListener
            }

            Log.i(TAG, "passwords match")
            val user = UserData(binding.etUsername.text.toString(), binding.etEmail.text.toString(), 0, binding.etPassword.text.toString(), 0)
            authViewModel.signUp(user)

        }
    }

    override fun onResume() {
        super.onResume()
        video.start()
    }
}

