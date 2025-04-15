package com.example.centauri.fragments.main_nav_frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centauri.R
import com.example.centauri.databinding.FragmentStudyLessonsListBinding
import com.example.centauri.models.GeminiViewModel
import com.example.centauri.models.rvItemType
import com.example.centauri.rvAdapterLesson
import com.example.centauri.rvItemsData
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.DbViewModel
import kotlinx.coroutines.launch

class StudyLessonsListFragment : Fragment() {

    private lateinit var binding: FragmentStudyLessonsListBinding
    private lateinit var lessonAdapter: rvAdapterLesson
    private lateinit var lessonList: Array<rvItemsData>

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

//        Log.i(TAG, "Gliding img")
//        Glide.with(view.context)
//            .load(R.drawable.stars_pattern)
//            .into(binding.imgStarBackground)






        updatingLessonList()
        Log.i(TAG, "assigning values to the adapter")
        lessonAdapter = rvAdapterLesson(lessonList)
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = lessonAdapter

        Log.i(TAG, "notifying the adapter")
        lessonAdapter.notifyDataSetChanged()








        super.onViewCreated(view, savedInstanceState)
    }


    fun updatingLessonList(doUpdate: Boolean = false){
        Log.i(TAG, "assigning values to the list")
        lessonList = arrayOf<rvItemsData>(
            rvItemsData(rvItemType.PART,  1, getString(R.string.part_1), R.drawable.ic_astronomy,4),
            rvItemsData(rvItemType.LESSON, 1, getString(R.string.lesson1), R.drawable.ic_astronomy,4),
            rvItemsData(rvItemType.LESSON, 2, getString(R.string.lesson2), R.drawable.ic_universe,3),
            rvItemsData(rvItemType.LESSON, 3, getString(R.string.lesson3), R.drawable.ic_explore,3),
            rvItemsData(rvItemType.LESSON, 4, getString(R.string.lesson4), R.drawable.ic_telescope,3),
            rvItemsData(rvItemType.TEST, 1, "Test of Part 1", R.drawable.ic_test,4),


            rvItemsData(rvItemType.PART, 2, getString(R.string.part_2), R.drawable.ic_test,2),
            rvItemsData(rvItemType.LESSON, 5, getString(R.string.lesson5), R.drawable.ic_astronaut3,2),
            rvItemsData(rvItemType.LESSON, 6,getString(R.string.lesson6), R.drawable.ic_earth2,2),
            rvItemsData(rvItemType.LESSON, 7, getString(R.string.lesson7), R.drawable.ic_some_planets,2),
//            rvItemsData(false, true, 4, getString(R.string.lesson8), R.drawable.ic_some_planets,4),
            rvItemsData(rvItemType.TEST, 2, "Test of Part 2", R.drawable.ic_test,2),

            rvItemsData(rvItemType.PART, 3, getString(R.string.part_3), R.drawable.ic_test,2),
            rvItemsData(rvItemType.LESSON, 8, getString(R.string.lesson9), R.drawable.ic_sun2,2),
            rvItemsData(rvItemType.LESSON, 9, getString(R.string.lesson10), R.drawable.ic_astrophysics,2),
            rvItemsData(rvItemType.LESSON, 10, getString(R.string.lesson11), R.drawable.ic_falling_star,2),
            rvItemsData(rvItemType.LESSON, 11, getString(R.string.lesson12), R.drawable.ic_black_hole,2),
            rvItemsData(rvItemType.TEST,  3, "Test of Part 3", R.drawable.ic_test,2),

            rvItemsData(rvItemType.PART, 4, getString(R.string.part_4), R.drawable.ic_test,2),
            rvItemsData(rvItemType.LESSON, 12, getString(R.string.lesson13), R.drawable.ic_milky_way,2),
            rvItemsData(rvItemType.LESSON, 13,getString(R.string.lesson14), R.drawable.ic_galaxy2,2),
            rvItemsData(rvItemType.LESSON, 14, getString(R.string.lesson15), R.drawable.ic_galaxy,2),
            rvItemsData(rvItemType.LESSON, 15, getString(R.string.lesson16), R.drawable.ic_space,2),
            rvItemsData(rvItemType.TEST, 4, "Test of Part 4", R.drawable.ic_space,2)
        )

        if (doUpdate){
            lessonAdapter.notifyDataSetChanged()
        }
    }
}