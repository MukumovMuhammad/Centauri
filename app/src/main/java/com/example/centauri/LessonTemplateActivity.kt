package com.example.centauri

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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




        val lesson_number = intent.getIntExtra("lesson_number", 1)
        val partsNumber = intent.getIntExtra("partsNumber", 1)

        var lesson_img_preview = ImageView(this, null, 0, R.style.CircleImageView)
        lesson_img_preview.setImageResource(resources.getIdentifier("lesson1_img_preview", "drawable", packageName))

        //                The Solutation to the heights problem
        val imageLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics
            ).toInt()
        )
        lesson_img_preview.layoutParams = imageLayoutParams

        binding.llmain.addView(lesson_img_preview)

        for (i in 1..partsNumber){
            var lesson_title = TextView(this)
            var lesson_text = TextView(this)
            lesson_title.text = resources.getString(resources.getIdentifier("lesson${lesson_number}_title_$i", "string", packageName))
            lesson_text.text = resources.getString(resources.getIdentifier("lesson${lesson_number}_text_$i", "string", packageName))

            lesson_title.setTextAppearance(R.style.LessonTitleStyle)
            lesson_text.setTextAppearance(R.style.LessonTextStyle)

            binding.llmain.addView(lesson_title)
            if(i < partsNumber){
                var lesson_img = ImageView(this, null, 0, R.style.CircleImageView)
                lesson_img.setImageResource(resources.getIdentifier("lesson${lesson_number}_img$i", "drawable", packageName))

                lesson_img.layoutParams = imageLayoutParams

                binding.llmain.addView(lesson_img)
            }

            binding.llmain.addView(lesson_text)


        }

    }
}