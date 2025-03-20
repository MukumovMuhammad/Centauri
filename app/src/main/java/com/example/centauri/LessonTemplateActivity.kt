package com.example.centauri

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.centauri.databinding.ActivityLessonTemplateBinding

class LessonTemplateActivity : AppCompatActivity() {
    private lateinit  var binding : ActivityLessonTemplateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLessonTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        for (i in 1..4){
            var lesson_title = TextView(this)
            var lesson_text = TextView(this)
            lesson_title.text = resources.getString(resources.getIdentifier("lesson1_title_$i", "string", packageName))
            lesson_text.text = resources.getString(resources.getIdentifier("lesson1_text_$i", "string", packageName))

            lesson_title.setTextAppearance(R.style.LessonTitleStyle)
            lesson_text.setTextAppearance(R.style.LessonTextStyle)

            binding.llmain.addView(lesson_title)
            binding.llmain.addView(lesson_text)
        }

    }
}