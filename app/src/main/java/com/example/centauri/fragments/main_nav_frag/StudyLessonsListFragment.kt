package com.example.centauri.fragments.main_nav_frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centauri.R
import com.example.centauri.databinding.FragmentStudyLessonsListBinding
import com.example.centauri.rvAdapterLesson
import com.example.centauri.rvItemsData

class StudyLessonsListFragment : Fragment() {

    private lateinit var binding: FragmentStudyLessonsListBinding
    private lateinit var lessonAdapter: rvAdapterLesson
    private lateinit var lessonList: Array<rvItemsData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyLessonsListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Log.i("StudyLessonsListFragment_TAG", "Gliding img")
//        Glide.with(view.context)
//            .load(R.drawable.stars_pattern)
//            .into(binding.imgStarBackground)

        Log.i("StudyLessonsListFragment_TAG", "assigning values to the list")
        lessonList = arrayOf<rvItemsData>(
            rvItemsData(false, false, 1, getString(R.string.lesson1), R.drawable.ic_astronomy,4),
            rvItemsData(false, false, 2, getString(R.string.lesson2), R.drawable.ic_universe,4),
            rvItemsData(false, false, 3, getString(R.string.lesson3), R.drawable.ic_explore,4),
            rvItemsData(false, false, 4, getString(R.string.lesson4), R.drawable.ic_telescope,4),
            rvItemsData(true, false, 1, "Test of Part 1", R.drawable.ic_test,4),

            rvItemsData(false, true, 1, getString(R.string.lesson5), R.drawable.ic_astronaut3,4),
            rvItemsData(false, true, 2,getString(R.string.lesson6), R.drawable.ic_earth2,4),
            rvItemsData(false, true, 3, getString(R.string.lesson7), R.drawable.ic_some_planets,4),
//            rvItemsData(false, true, 4, getString(R.string.lesson8), R.drawable.ic_some_planets,4),
            rvItemsData(true, true, 2, "Test of Part 2", R.drawable.ic_test,4),

            rvItemsData(false, true, 1, getString(R.string.lesson9), R.drawable.ic_sun2,4),
            rvItemsData(false, true, 2, getString(R.string.lesson10), R.drawable.ic_astrophysics,4),
            rvItemsData(false, true, 3, getString(R.string.lesson11), R.drawable.ic_falling_star,4),
            rvItemsData(false, true, 4, getString(R.string.lesson12), R.drawable.ic_black_hole,4),
            rvItemsData(true, true, 3, "Test of Part 3", R.drawable.ic_test,4),

            rvItemsData(false, true, 1, getString(R.string.lesson13), R.drawable.ic_milky_way,4),
            rvItemsData(false, true, 2,getString(R.string.lesson14), R.drawable.ic_galaxy2,4),
            rvItemsData(false, true, 3, getString(R.string.lesson15), R.drawable.ic_galaxy,4),
            rvItemsData(false, true, 4, getString(R.string.lesson16), R.drawable.ic_space,4),
            rvItemsData(true, true, 4, "Test of Part 4", R.drawable.ic_test,4)
        )


        Log.i("StudyLessonsListFragment_TAG", "assigning values to the adapter")
        lessonAdapter = rvAdapterLesson(lessonList)
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = lessonAdapter

        Log.i("StudyLessonsListFragment_TAG", "notifying the adapter")
        lessonAdapter.notifyDataSetChanged()







        super.onViewCreated(view, savedInstanceState)
    }
}