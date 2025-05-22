package com.example.centauri.fragments.main_nav_frag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centauri.R

import com.example.centauri.databinding.FragmentNasaNewsBinding
import com.example.centauri.rv.ApodNewsData
import com.example.centauri.rv.rvAdapterNasaNews
import com.example.centauri.models.DbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NasaNewsFragment : Fragment() {

    companion object{
        const val TAG = "NasaNewsFragment"
    }

    private lateinit var newsAdapter: rvAdapterNasaNews
    private lateinit var video: VideoView
    private val nasaNewsList = mutableListOf<ApodNewsData>()


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
        newsAdapter = rvAdapterNasaNews(nasaNewsList)
        binding.rvNasaNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNasaNews.adapter = newsAdapter



        lifecycleScope.launch {
            var nasaApod : List<ApodNewsData> = DbViewModel().getNasa10News()
            withContext(Dispatchers.Main){
                Log.i(TAG, "nasaApod got datas: datas =  $nasaApod")
                binding.rvNasaNews.visibility = View.VISIBLE
                nasaNewsList.addAll(nasaApod)
                Log.i(TAG, "nasaNewsList got datas: datas =  $nasaNewsList")
                newsAdapter.notifyDataSetChanged()
                binding.darkOverlay.visibility = View.GONE
                binding.loading.visibility = View.GONE
            }

        }
    }


}