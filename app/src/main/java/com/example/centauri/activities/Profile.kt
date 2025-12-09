package com.example.centauri.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.centauri.DialogWindows
import com.example.centauri.R
import com.example.centauri.fragments.profile_frags.ProfileFragment
import com.example.centauri.models.AuthViewModel
import com.example.centauri.models.DbViewModel
import com.example.centauri.models.UserData

class Profile : AppCompatActivity() {

    private lateinit var  authViewModel: AuthViewModel
    private lateinit var  db: DbViewModel
    private lateinit var  dialogWindows: DialogWindows




    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("ProfileActivity_TAG", "onCreate() called with: savedInstanceState = $savedInstanceState")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment(ProfileFragment())
        hide_statusbar()
    }

    private fun replaceFragment(frag: Fragment) {
        Log.i("ProfileActivity_TAG", "replaceFragment() called with: frag = $frag")
//        var bundle: Bundle = Bundle()
//        bundle.putParcelable("userData", userData)
//        frag.arguments = bundle
        val FragManager = supportFragmentManager;
        val FragTransition = FragManager.beginTransaction()
        FragTransition.replace(R.id.profile_fr_container, frag)
        FragTransition.commit()
    }


    @SuppressLint("ResourceAsColor")
    fun hide_statusbar(){
        // Obtain the WindowInsetsController from the window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // Set the behavior to allow the bars to reappear with a swipe
        windowInsetsController.systemBarsBehavior =
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
        Log.v("MainActivity_TAG", "language that is used is ${LocaleHelper.getSavedLanguage(newBase)}")
    }
}