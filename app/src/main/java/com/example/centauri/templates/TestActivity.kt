package com.example.centauri.templates

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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
import com.example.centauri.databinding.ActivityTestBinding
import com.example.centauri.models.GeminiViewModel
import com.example.firebasetodoapp.DbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding;
    private lateinit var geminiModel: GeminiViewModel;
    private lateinit var theTest : TestQuestion;
    private lateinit var userEmail: String
    private  var  test_number: Int = 1;

    private var userAnswer: Int = 0
    private var wasLastAnswerCorrect: Boolean = true
    private var currentTest: Int = 1;
    private var correctAnswered: Int = 0
    private var dialog: DialogWindows = DialogWindows(this)
    private var dbViewModel: DbViewModel = DbViewModel()


    companion object {
        const val TAG = "TestActivity_TAG"
        const val TEST_NUMBER = 10
    }
    enum class testState{
        LOADING,
        CHECKING,
        READY,
        ANSWERED,
        ERROR,
        FINISHED
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

        test_number = intent.getIntExtra("lesson_number", 1)
        userEmail = intent.getStringExtra("userEmail").toString()


        hide_statusbar()

        resetBtns()

        lifecycleScope.launch {
            setUpTest(testState.CHECKING)
            var isWorking = geminiModel.isGemeniWorking()
            withContext(Dispatchers.Main){
                if (!isWorking){
                    setUpTest(testState.ERROR)
                }else{
                    setUpTest(testState.LOADING)
                    delay(500)
                    var lessonsRange: ArrayList<Int> = arrayListOf()
                    when (test_number){
                        2 -> lessonsRange = arrayListOf(5,6,7)
                        3 -> lessonsRange = arrayListOf(8,9,10,11)
                        4 -> lessonsRange = arrayListOf(12,13,14,15)
                    }
                    dbViewModel.getLessonTitlesAndContext(lessonsRange){text ->
                       lifecycleScope.launch {
                           theTest = geminiModel.startNewTest(this@TestActivity, text)

                           Log.i(TAG, "theTest is $theTest")

                           if (theTest.feedback == "ERROR"){
                               setUpTest(testState.ERROR)
                           }
                           else{
                               setUpTest(testState.READY)
                           }

                       }
                    }


                }
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
                binding.questionText.text = theTest.question
                binding.textOptionA.text = theTest.A
                binding.textOptionB.text = theTest.B
                binding.textOptionC.text = theTest.C
                binding.textOptionD.text = theTest.D
            }
            testState.LOADING ->{
                resetBtns()
                binding.questionText.text = R.string.loading_question.toString()
                binding.textOptionA.text = "A"
                binding.textOptionB.text = "B"
                binding.textOptionC.text = "C"
                binding.textOptionD.text = "D"

            }
            testState.CHECKING ->{
                binding.questionText.text = R.string.checking_ai.toString()
            }
            testState.ANSWERED ->{

                dialog.testResult(wasLastAnswerCorrect,theTest.feedback, object : DialogWindows.DialogCallback{
                    override fun onOkCLicked() {

                        if (currentTest != TEST_NUMBER){
                            setUpTest(testState.READY)
                        }
                        else{
                            setUpTest(testState.LOADING)
                            setUpTest(testState.FINISHED)
                        }
                        resetBtns()
                    }
                })
            }

            testState.ERROR -> {
                dialog.testResult(false,"Sorry the AI is not working try again later", object: DialogWindows.DialogCallback{
                    override fun onOkCLicked() {
                        finish()
                    }
                }, "ERROR!")
            }

            testState.FINISHED -> {
                lifecycleScope.launch {

                    var result: String = geminiModel.testResult(this@TestActivity,correctAnswered )
                    withContext(Dispatchers.Main){
                        dialog.testResult(correctAnswered >= 8, result, object : DialogWindows.DialogCallback{
                            override fun onOkCLicked() {
                                Log.i(TAG, "testResult AlertDialog the onClicked fun is on work")
                                if (correctAnswered >= 8){
                                    Log.i(TAG, "User past Test and now the result will be stored to db")
                                    dbViewModel.testPastUser(userEmail,test_number, callback = {success ->
                                        if (success){
                                            Log.i(TAG, "testPastUser is success")
                                            finish()
                                        }
                                        else{
                                            dialog.testResult(false,"Sorry but the result hasn't been stored in the DataBase for some  unknown reasons, Could you please check your internet connection!", object: DialogWindows.DialogCallback{
                                                override fun onOkCLicked() {
                                                    finish()
                                                }
                                            }, "ERROR!")
                                        }
                                    })
                                }


                            }
                        }, "Results")
                    }
                }
            }

            else ->{

            }
        }

    }

    private fun checkTest(selected: Int){


        Log.i(TAG, "User selected the option $selected")

        userAnswer = selected
        wasLastAnswerCorrect = userAnswer == theTest.answer
        currentTest++;
        lifecycleScope.launch {
//            setUpTest(testState.LOADING)
            theTest = geminiModel.nextTest(this@TestActivity, userAnswer, wasLastAnswerCorrect)
            withContext(Dispatchers.Main){
                if (theTest.feedback == "ERROR"){
                    setUpTest(testState.ERROR)
                }
                else{
                    setUpTest(testState.ANSWERED)
                }
            }

        }
        circleImg()
        if (wasLastAnswerCorrect){
            correctAnswer()
        }
        else{
            wrongAnswer()
        }

    }

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



    private fun correctAnswer(){
        correctAnswered++;
        when (userAnswer){
            0 -> binding.textOptionA.setTextColor(ContextCompat.getColor(this, R.color.bright_green))
            1 -> binding.textOptionB.setTextColor(ContextCompat.getColor(this, R.color.bright_green))
            2 -> binding.textOptionC.setTextColor(ContextCompat.getColor(this, R.color.bright_green))
            3 -> binding.textOptionD.setTextColor(ContextCompat.getColor(this, R.color.bright_green))
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

    private fun wrongAnswer(){
        when (userAnswer){
            0 -> binding.textOptionA.setTextColor(ContextCompat.getColor(this, R.color.bright_red))
            1 -> binding.textOptionB.setTextColor(ContextCompat.getColor(this, R.color.bright_red))
            2 -> binding.textOptionC.setTextColor(ContextCompat.getColor(this, R.color.bright_red))
            3 -> binding.textOptionD.setTextColor(ContextCompat.getColor(this, R.color.bright_red))
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