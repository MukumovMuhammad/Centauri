package com.example.centauri.templates

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.centauri.DialogWindows
import com.example.centauri.R
import com.example.centauri.databinding.ActivityLessonTemplateBinding
import com.example.centauri.databinding.ActivityTestBinding
import com.example.centauri.models.GeminiViewModel
import com.google.firebase.database.core.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var geminiModel: GeminiViewModel
    private lateinit var the_test : TestQuestion
    private var userAnswer: Int = 0
    private var currentTest: Int = 1;
    private var correctAnswered: Int = 0
    private var dialog: DialogWindows = DialogWindows(this)

    companion object {
        const val TAG = "TestActivity_TAG"
    }
    enum class testState{
        LOADING,
        READY,
        ERROR,
        ANSWERED
    }

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

        resetBtns()


        lifecycleScope.launch {
            setUpTest(testState.LOADING)
            the_test = geminiModel.startNewTest(this@TestActivity)
            withContext(Dispatchers.Main){
                setUpTest(testState.READY)
            }
         }


//////// Btn option on CLick listeners! ////////

        binding.optionA.setOnClickListener {checkTest(0)}
        binding.optionB.setOnClickListener {checkTest(1)}
        binding.optionC.setOnClickListener {checkTest(2)}
        binding.optionD.setOnClickListener {checkTest(3)}









    }

    private fun setUpTest(state: testState){
        when (state){
            testState.READY -> {
                binding.questionText.text = the_test.question
                binding.textOptionA.text = the_test.A
                binding.textOptionB.text = the_test.B
                binding.textOptionC.text = the_test.C
                binding.textOptionD.text = the_test.D
            }
            testState.LOADING ->{
                resetBtns()
                binding.questionText.text = "Loading question..."
                binding.textOptionA.text = "A"
                binding.textOptionB.text = "B"
                binding.textOptionC.text = "C"
                binding.textOptionD.text = "D"

            }
            testState.ANSWERED ->{
                dialog.SimpleAlertDialog("Answer",the_test.feedback, object : DialogWindows.DialogCallback{
                    override fun onOkCLicked() {
                        setUpTest(testState.READY)
                        resetBtns()
                    }
                })
            }
            else ->{

            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun resetBtns(){
        binding.optionAImage.setBackgroundResource(R.drawable.shape_unselected_circle)
        binding.optionBImage.setBackgroundResource(R.drawable.shape_unselected_circle)
        binding.optionCImage.setBackgroundResource(R.drawable.shape_unselected_circle)
        binding.optionDImage.setBackgroundResource(R.drawable.shape_unselected_circle)


        binding.textOptionA.setTextColor(ContextCompat.getColor(this, R.color.starry_white))
        binding.textOptionB.setTextColor(ContextCompat.getColor(this, R.color.starry_white))
        binding.textOptionC.setTextColor(ContextCompat.getColor(this, R.color.starry_white))
        binding.textOptionD.setTextColor(ContextCompat.getColor(this, R.color.starry_white))
    }


    @SuppressLint("ResourceAsColor")
    private fun correctAnswer(){
        correctAnswered++;
        when (userAnswer){
            0 -> binding.textOptionA.setTextColor(ContextCompat.getColor(this, R.color.dusty_green))
            1 -> binding.textOptionB.setTextColor(ContextCompat.getColor(this, R.color.dusty_green))
            2 -> binding.textOptionC.setTextColor(ContextCompat.getColor(this, R.color.dusty_green))
            3 -> binding.textOptionD.setTextColor(ContextCompat.getColor(this, R.color.dusty_green))
        }
    }
    private fun circleImg(){
        when (userAnswer){
            0 -> binding.optionAImage.setBackgroundResource(R.drawable.shape_selected_circle)
            1 -> binding.optionBImage.setBackgroundResource(R.drawable.shape_selected_circle)
            2 -> binding.optionCImage.setBackgroundResource(R.drawable.shape_selected_circle)
            3 -> binding.optionDImage.setBackgroundResource(R.drawable.shape_selected_circle)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun wrongAnswer(){
        when (userAnswer){
            0 -> binding.textOptionA.setTextColor(ContextCompat.getColor(this, R.color.dusty_red))
            1 -> binding.textOptionB.setTextColor(ContextCompat.getColor(this, R.color.dusty_red))
            2 -> binding.textOptionC.setTextColor(ContextCompat.getColor(this, R.color.dusty_red))
            3 -> binding.textOptionD.setTextColor(ContextCompat.getColor(this, R.color.dusty_red))
        }
    }


    private fun checkTest(selected: Int){


        Log.i(TAG, "User selected the option $selected")

        userAnswer = selected
        lifecycleScope.launch {
            if (currentTest <= 9)
            {
                the_test = geminiModel.nextTest(this@TestActivity,userAnswer )
                withContext(Dispatchers.Main){
                    setUpTest(testState.ANSWERED)
                    currentTest++;
                }
            }
            else{
                var result = geminiModel.testResult(this@TestActivity, correctAnswered)
                withContext(Dispatchers.Main){
                    dialog.SimpleAlertDialog("The result", result, object : DialogWindows.DialogCallback{
                        override fun onOkCLicked() {
                            finish()
                        }
                    })
                }
            }
        }
        circleImg()
        if (userAnswer == the_test.answer){
            correctAnswer()
        }
        else{
            wrongAnswer()
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