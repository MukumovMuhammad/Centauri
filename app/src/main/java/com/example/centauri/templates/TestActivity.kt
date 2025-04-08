package com.example.centauri.templates

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.centauri.R
import com.example.centauri.databinding.ActivityLessonTemplateBinding
import com.example.centauri.databinding.ActivityTestBinding
import com.example.centauri.models.GeminiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var geminiModel: GeminiViewModel
    private lateinit var the_test : TestQuestion
    override fun onCreate(savedInstanceState: Bundle?) {
        geminiModel = GeminiViewModel()
        binding = ActivityTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        hide_statusbar()
        binding.questionText.text = "Loading question..."
        binding.textOptionA.text = "A"
        binding.textOptionB.text = "B"
        binding.textOptionC.text = "C"
        binding.textOptionD.text = "D"


        lifecycleScope.launch {
            the_test = geminiModel.startNewTest(this@TestActivity)
            withContext(Dispatchers.Main){
                binding.questionText.text = the_test.question
                binding.textOptionA.text = the_test.A
                binding.textOptionB.text = the_test.B
                binding.textOptionC.text = the_test.C
                binding.textOptionD.text = the_test.D
            }
         }


//////// Btn option on CLick listeners! ////////

        binding.optionA.setOnClickListener {checkTest(0)}
        binding.optionB.setOnClickListener {checkTest(1)}
        binding.optionC.setOnClickListener {checkTest(2)}
        binding.optionD.setOnClickListener {checkTest(3)}









    }


    private fun checkTest(selected: Int){
        if (selected == the_test.answer){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
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
}