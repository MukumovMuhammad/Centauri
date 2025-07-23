package com.example.centauri.fragments.main_nav_frag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centauri.R
import com.example.centauri.databinding.FragmentStudyLessonsListBinding
import com.example.centauri.rv.rvItemType
import com.example.centauri.rv.rvAdapterLesson
import com.example.centauri.rv.rvItemsLessonsData
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class StudyLessonsListFragment : Fragment() {
    private lateinit var binding: FragmentStudyLessonsListBinding
    private lateinit var lessonAdapter: rvAdapterLesson
    private lateinit var lessonList: Array<rvItemsLessonsData>
    private  var rewardedAd: RewardedAd? = null
    private var testPast: Int = 0;
    companion object{
        const val TAG = "StudyLessonsListFragment_TAG"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyLessonsListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updatingLessonList()
        Log.i(TAG, "assigning values to the adapter")
        lessonAdapter = rvAdapterLesson(lessonList, ::OnTestClicked)
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = lessonAdapter

        Log.i(TAG, "notifying the adapter")
        lessonAdapter.notifyDataSetChanged()

        super.onViewCreated(view, savedInstanceState)

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    rewardedAd = null
                }
            },
        )

        rewardedAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d(TAG, "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show.
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    rewardedAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdClicked() {
                    // Called when an ad is clicked.
                    Log.d(TAG, "Ad was clicked.")
                }
            }
    }

    fun OnTestClicked(intent: Intent){
        rewardedAd?.show(
            requireActivity(),
            OnUserEarnedRewardListener { rewardItem ->
                Log.d(TAG, "User earned the reward.")
                // Handle the reward.
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                startActivity(intent)
            },
        )
    }

    fun updatingLessonList(doUpdate: Boolean = false){
        Log.i(TAG, "assigning values to the list")
        lessonList = arrayOf<rvItemsLessonsData>(
            rvItemsLessonsData(rvItemType.PART,  1, getString(R.string.part_1), R.drawable.ic_astronomy,4),
            rvItemsLessonsData(rvItemType.LESSON, 1, getString(R.string.lesson1), R.drawable.ic_astronomy,4),
            rvItemsLessonsData(rvItemType.LESSON, 2, getString(R.string.lesson2), R.drawable.ic_universe,3),
            rvItemsLessonsData(rvItemType.LESSON, 3, getString(R.string.lesson3), R.drawable.ic_explore,3),
            rvItemsLessonsData(rvItemType.LESSON, 4, getString(R.string.lesson4), R.drawable.ic_telescope,3),
            rvItemsLessonsData(rvItemType.TEST, 1, "Test of Part 1", R.drawable.ic_test,4),

            rvItemsLessonsData(rvItemType.PART, 2, getString(R.string.part_2), R.drawable.ic_test,2),
            rvItemsLessonsData(rvItemType.LESSON, 5, getString(R.string.lesson5), R.drawable.ic_astronaut3,2),
            rvItemsLessonsData(rvItemType.LESSON, 6,getString(R.string.lesson6), R.drawable.ic_earth2,2),
            rvItemsLessonsData(rvItemType.LESSON, 7, getString(R.string.lesson7), R.drawable.ic_some_planets,2),
//            rvItemsData(false, true, 4, getString(R.string.lesson8), R.drawable.ic_some_planets,4),
            rvItemsLessonsData(rvItemType.TEST, 2, "Test of Part 2", R.drawable.ic_test,2),

            rvItemsLessonsData(rvItemType.PART, 3, getString(R.string.part_3), R.drawable.ic_test,2),
            rvItemsLessonsData(rvItemType.LESSON, 8, getString(R.string.lesson9), R.drawable.ic_sun2,2),
            rvItemsLessonsData(rvItemType.LESSON, 9, getString(R.string.lesson10), R.drawable.ic_astrophysics,2),
            rvItemsLessonsData(rvItemType.LESSON, 10, getString(R.string.lesson11), R.drawable.ic_falling_star,2),
            rvItemsLessonsData(rvItemType.LESSON, 11, getString(R.string.lesson12), R.drawable.ic_black_hole,2),
            rvItemsLessonsData(rvItemType.TEST,  3, "Test of Part 3", R.drawable.ic_test,2),

            rvItemsLessonsData(rvItemType.PART, 4, getString(R.string.part_4), R.drawable.ic_test,2),
            rvItemsLessonsData(rvItemType.LESSON, 12, getString(R.string.lesson13), R.drawable.ic_milky_way,2),
            rvItemsLessonsData(rvItemType.LESSON, 13,getString(R.string.lesson14), R.drawable.ic_galaxy2,2),
            rvItemsLessonsData(rvItemType.LESSON, 14, getString(R.string.lesson15), R.drawable.ic_galaxy,2),
            rvItemsLessonsData(rvItemType.LESSON, 15, getString(R.string.lesson16), R.drawable.ic_space,2),
            rvItemsLessonsData(rvItemType.TEST, 4, "Test of Part 4", R.drawable.ic_space,2)
        )
        if (doUpdate){
            lessonAdapter.notifyDataSetChanged()
        }
    }
}

