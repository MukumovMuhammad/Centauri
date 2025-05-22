package com.example.centauri.activities.templates

import android.annotation.SuppressLint
import android.content.Context

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.centauri.R
import com.example.centauri.databinding.ActivityLessonTemplateBinding
import com.example.centauri.models.DbViewModel

class LessonTemplateActivity : AppCompatActivity() {
    private lateinit  var binding : ActivityLessonTemplateBinding

    private lateinit var dbViewModel: DbViewModel
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

        hide_statusbar()

        val lesson_number = intent.getIntExtra("lesson_number", 1)
        val partsNumber = intent.getIntExtra("partsNumber", 1)
        dbViewModel = DbViewModel()
        var lesson_img_preview = ImageView(this, null, 0, R.style.CircleImageView)

        //                The Solutation to the heights problem
        val imageLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics
            ).toInt()
        )
        lesson_img_preview.layoutParams = imageLayoutParams

        binding.customTitle.text = resources.getString(resources.getIdentifier("lesson${lesson_number}", "string", packageName))


        for (i in 1..partsNumber){
            var lesson_title = TextView(this)
            var lesson_text = TextView(this)

            if (lesson_number > 4){

                dbViewModel.getLessonTitles(lesson_number, this){titles ->
                    lesson_title.text = titles[i-1]
                }

                dbViewModel.getLessonContext(lesson_number, this){contexts ->
                    lesson_text.text = contexts[i-1]
                }

            }else{

                lesson_title.text = LocaleHelper.getLocalizedString(this,"lesson${lesson_number}_title_$i")
                lesson_text.text = LocaleHelper.getLocalizedString(this,"lesson${lesson_number}_text_$i")
            }


            lesson_title.setTextAppearance(R.style.LessonTitleStyle)
            lesson_text.setTextAppearance(R.style.LessonTextStyle)

            binding.textHold.addView(lesson_title)

//    ----------------          IMAGES GLIDING ---------------- //
                var lesson_img = ImageView(this, null, 0, R.style.CircleImageView)


//                lesson_img.setImageResource(resources.getIdentifier("lesson${lesson_number}_img$i", "drawable", packageName))

                lesson_img.layoutParams = imageLayoutParams




                dbViewModel.getLessonImgs(lesson_number) { images ->
                    if (i == 1){
                        Glide.with(this)
                            .load(images[0])
                            .placeholder(R.drawable.ic_img_default)
                            .error(R.drawable.ic_disconected)
                            .into(binding.imgPreview)
                    }
                        Glide.with(this)
                            .load(images[i])
                            .placeholder(R.drawable.ic_img_default)
                            .error(R.drawable.ic_disconected)
                            .into(lesson_img)
                }


            binding.textHold.addView(lesson_img)
            binding.textHold.addView(lesson_text)


        }


       var arrowBack: ImageView =  findViewById<ImageView>(R.id.arrow_back)


//        Arrow back
        arrowBack.setOnClickListener{
            finish()
        }

    }

    @SuppressLint("ResourceAsColor")
    fun hide_statusbar(){
        // Obtain the WindowInsetsController from the window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Hide both the status bar and the navigation bar
        windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
        // Set the behavior to allow the bars to reappear with a swipe
        windowInsetsController?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


        // Set the status bar and navigation bar colors to transparent
        window.statusBarColor = android.R.color.transparent
        window.navigationBarColor = android.R.color.transparent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase!!, LocaleHelper.getSavedLanguage(newBase)))
    }


}