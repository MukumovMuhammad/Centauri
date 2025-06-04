package com.example.centauri.fragments.main_nav_frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.centauri.R
import com.example.centauri.databinding.FragmentNasaNewsBinding
import com.example.centauri.databinding.FragmentOtherMaterialsBinding

class OtherMaterials : Fragment() {

    private lateinit var binding : FragmentOtherMaterialsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtherMaterialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        Video Settings
        binding.videoBg.setVideoPath("android.resource://" + requireContext().packageName + "/" + R.raw.video_bg_ai_generated)

        // Adjust size to fill the
        binding.videoBg.setOnPreparedListener { mp ->
            mp.isLooping = true
        }
        binding.videoBg.start()

    }


}