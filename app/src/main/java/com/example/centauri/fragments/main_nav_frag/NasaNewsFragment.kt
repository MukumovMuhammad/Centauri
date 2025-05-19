package com.example.centauri.fragments.main_nav_frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.centauri.R
import com.example.centauri.databinding.FragmentNasaNewsBinding
import com.example.centauri.models.ApodNewsData
import com.example.firebasetodoapp.DbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NasaNewsFragment : Fragment() {

    private lateinit var binding : FragmentNasaNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNasaNewsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launch {
            var nasaApod : ApodNewsData = DbViewModel().getNasaApod()
            withContext(Dispatchers.Main){
                binding.darkOverlay.visibility = View.GONE
                binding.loading.visibility = View.GONE

                Glide.with(requireContext())
                    .load(nasaApod.url)
                    .placeholder(R.drawable.ic_img_default)
                    .error(R.drawable.ic_img_default)
                    .into(binding.apodImg)
                binding.tTitle.text = nasaApod.title
                binding.tText.text = nasaApod.explanation
            }

        }
    }


}