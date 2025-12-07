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
import com.example.centauri.DialogWindows
import com.example.centauri.R

import com.example.centauri.databinding.FragmentNasaNewsBinding
import com.example.centauri.rv.ApodNewsData
import com.example.centauri.rv.rvAdapterNasaNews
import com.example.centauri.models.DbViewModel
import com.example.centauri.models.GeminiViewModel
import com.example.centauri.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NasaNewsFragment : Fragment() {

    companion object{
        const val TAG = "NasaNewsFragment_TAG"
    }

    enum class ChipBtnState {
        ALL,
        TODAY,
        SAVES
    }

    private lateinit var newsAdapter: rvAdapterNasaNews
    private val nasaNewsList = ArrayList<ApodNewsData>()
    private  var isGeminiWorking: Boolean = false
    private lateinit var dialogWindows: DialogWindows


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
        dialogWindows = DialogWindows(requireContext())
        binding.loadingText.text = getString(R.string.loading_data)
        newsAdapter.refreshUserData { success->
            lifecycleScope.launch {
                binding.loadingText.text = getString(R.string.checking_ai)
                Log.i(TAG, "trying to see is Gemini working")
                isGeminiWorking = GeminiViewModel().isGemeniWorking()
                Log.i(TAG, "checked and Gemini working is $isGeminiWorking")

                withContext(Dispatchers.Main){
                    if (isGeminiWorking){
                        chipBtnClicked(ChipBtnState.TODAY)
                        getNews(ChipBtnState.TODAY)
                    }
                    else{
                        dialogWindows.showSpaceDialog(getString(R.string.error), getString(R.string.failed_to_translate),object :
                            DialogWindows.DialogCallback {
                            override fun onOkCLicked() {

                            }
                        }, showCancel = false)
                    }
                }
            }
        }






        binding.btnAllNews.setOnClickListener {
            chipBtnClicked(ChipBtnState.ALL)
            getNews(ChipBtnState.ALL)
        }
        binding.btnTodayNews.setOnClickListener {
            chipBtnClicked(ChipBtnState.TODAY)
            getNews(ChipBtnState.TODAY)
        }
        binding.btnSavedNews.setOnClickListener {
            chipBtnClicked(ChipBtnState.SAVES)
            getNews(ChipBtnState.SAVES)
        }
    }


    private fun chipBtnClicked(chipBtnState: ChipBtnState){
        when(chipBtnState){
            ChipBtnState.ALL -> {
                binding.btnAllNews.chipBackgroundColor = resources.getColorStateList(R.color.blue_gradient_start)
                binding.btnTodayNews.chipBackgroundColor = resources.getColorStateList(R.color.white)
                binding.btnSavedNews.chipBackgroundColor = resources.getColorStateList(R.color.white)
            }
            ChipBtnState.TODAY -> {
                binding.btnAllNews.chipBackgroundColor = resources.getColorStateList(R.color.white)
                binding.btnTodayNews.chipBackgroundColor = resources.getColorStateList(R.color.blue_gradient_start)
                binding.btnSavedNews.chipBackgroundColor = resources.getColorStateList(R.color.white)

            }
            ChipBtnState.SAVES -> {
                binding.btnAllNews.chipBackgroundColor = resources.getColorStateList(R.color.white)
                binding.btnTodayNews.chipBackgroundColor = resources.getColorStateList(R.color.white)
                binding.btnSavedNews.chipBackgroundColor = resources.getColorStateList(R.color.blue_gradient_start)

            }
        }
    }


    private fun getNews(chipBtnState: ChipBtnState){
        binding.darkOverlay.visibility = View.VISIBLE
        binding.loading.visibility = View.VISIBLE
        binding.loadingText.visibility = View.VISIBLE
        binding.loadingText.text = getString(R.string.loading_data)

        when(chipBtnState){
            ChipBtnState.ALL -> {

                lifecycleScope.launch {
                    var nasaApod : List<ApodNewsData> = DbViewModel().getNasa10News(requireContext(), isGeminiWorking)
                    withContext(Dispatchers.Main){
                        Log.i(TAG, "nasaApod got datas: datas =  $nasaApod")
                        binding.rvNasaNews.visibility = View.VISIBLE
                        nasaNewsList.clear()
                        nasaNewsList.addAll(nasaApod)
                        Log.i(TAG, "nasaNewsList got datas: datas =  $nasaNewsList")
                        newsAdapter.notifyDataSetChanged()
                        binding.darkOverlay.visibility = View.GONE
                        binding.loading.visibility = View.GONE
                        binding.loadingText.visibility = View.GONE
                    }
                }
            }
            ChipBtnState.TODAY -> {
                lifecycleScope.launch {
                    var nasaApod : List<ApodNewsData> = DbViewModel().getNasaApod(requireContext(), isGeminiWorking)
                    withContext(Dispatchers.Main){
                        Log.i(TAG, "nasaApod got datas: datas =  $nasaApod")
                        binding.rvNasaNews.visibility = View.VISIBLE
                        nasaNewsList.clear()
                        nasaNewsList.addAll(nasaApod)
                        Log.i(TAG, "nasaNewsList got datas: datas =  $nasaNewsList")
                        newsAdapter.notifyDataSetChanged()
                        binding.darkOverlay.visibility = View.GONE
                        binding.loading.visibility = View.GONE
                        binding.loadingText.visibility = View.GONE
                    }
                }

            }
            ChipBtnState.SAVES -> {
                newsAdapter.showSavedOnce(context = requireContext(), {success->
                    binding.darkOverlay.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                    binding.loadingText.visibility = View.GONE

                    if (!success){
                        nasaNewsList.clear()
                        nasaNewsList.add(ApodNewsData("Something went wrong", "error","null", "null" ))
                    }
                })
            }
        }
    }


}