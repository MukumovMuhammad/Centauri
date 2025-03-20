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
import java.util.ArrayList

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
            rvItemsData(false, false, 1, "The Nature of Astronomy and Science", R.drawable.ic_universe),
            rvItemsData(false, false, 2, "The Universe: Large and Small Scales", R.drawable.ic_universe),
            rvItemsData(false, false, 3, "Observing the Sky: History of Astronomy", R.drawable.ic_universe),
            rvItemsData(false, false, 4, "Tools of Astronomy: Telescopes & Instruments", R.drawable.ic_universe),
            rvItemsData(true, false, 1, "Test of Part 1", R.drawable.ic_test),

            rvItemsData(false, true, 1, "Orbits & Gravity: The Foundation", R.drawable.ic_universe),
            rvItemsData(false, true, 2, "The Earth-Moon System", R.drawable.ic_universe),
            rvItemsData(false, true, 3, "The Inner Planets (Mercury, Venus, Earth, Mars)", R.drawable.ic_universe),
            rvItemsData(false, true, 4, "The Outer Planets & Beyond", R.drawable.ic_universe),
            rvItemsData(true, true, 1, "Test of Part 2", R.drawable.ic_test)
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