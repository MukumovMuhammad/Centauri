package com.example.centauri.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.centauri.R

class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        window.statusBarColor = Color.TRANSPARENT


        hide_statusbar()



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



    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp()|| super.onSupportNavigateUp()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase!!, LocaleHelper.getSavedLanguage(newBase)))
        Log.v("MainActivity_TAG", "language that is used is ${LocaleHelper.getSavedLanguage(newBase)}")
    }
}