package com.example.centauri.activities

import LocaleHelper
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.centauri.DialogWindows
import com.example.centauri.R
import com.example.centauri.databinding.ActivityMainBinding
import com.example.centauri.fragments.main_nav_frag.NasaNewsFragment
import com.example.centauri.fragments.main_nav_frag.OtherMaterials
import com.example.centauri.fragments.main_nav_frag.StudyLessonsListFragment
import com.example.centauri.models.AuthState
import com.example.centauri.models.AuthViewModel
import com.example.centauri.models.DbViewModel
import com.example.centauri.models.UserData
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

//    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var  authViewModel: AuthViewModel
    private lateinit var  db: DbViewModel
    private lateinit var  dialogWindows: DialogWindows

    private  var userData: UserData = UserData(null.toString(), null.toString(), 0, null.toString(), 0)


    private var isBackPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())

    private val resetBackPressed = Runnable {
        isBackPressedOnce = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {



        authViewModel = AuthViewModel()
        db = DbViewModel()
        dialogWindows = DialogWindows(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }

        if (authViewModel.authState.value == AuthState.Authenticated){
            db.getUserData(authViewModel.getCurrentUser()?.email.toString()){ user->
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.username_text).text = user.username
                userData = user
            }

        }
        else{

//            binding.navView.(0).findViewById<TextView>(R.id.nav_logout).text = getString(R.string.test)
            binding.navView.menu.findItem(R.id.nav_logout).title = getString(R.string.sign_up)
        }

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.main, binding.toolbar,
            R.string.navigation_drawer_close, R.string.navigation_drawer_open
        )

        binding.main.addDrawerListener(toggle)
//        toggle.syncState() // I comment this because it appears on the left side

        if (savedInstanceState == null) {
            replaceFragment(StudyLessonsListFragment())
        }


//        Click listening to open/close the side bar
        binding.hamburgerButton.setOnClickListener{
            if (binding.main.isDrawerOpen(GravityCompat.END)) {
             binding.main.closeDrawer(GravityCompat.END)
            } else {
                binding.main.openDrawer(GravityCompat.END)
            }
        }




//        These are for making icons colored
        binding.bottomNavigationView.itemIconTintList = null
        binding.navView.itemIconTintList = null


        binding.bottomNavigationView.selectedItemId = R.id.learn



        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.learn -> replaceFragment(StudyLessonsListFragment())
                R.id.news -> replaceFragment(NasaNewsFragment())
                R.id.extra -> replaceFragment(OtherMaterials())
                else -> {
                }
            }
            true
        }

        binding.navView.setNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.nav_language -> {
                    dialogWindows.langChoosing(){lang ->
                        val intent = intent
                        finish()
                        LocaleHelper.setLanguage(this,lang, intent)
                    }
                    true
                }

                R.id.nav_logout -> {
                    if (authViewModel.authState.value == AuthState.Authenticated){
                        authViewModel.signOut()

                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                    else{
                        var intent = Intent(this@MainActivity, AuthActivity::class.java)
                        startActivity(intent)

                    }
                    true
                }
            }

            binding.main.closeDrawer(GravityCompat.END)
            true
        }


        hide_statusbar()






    }


    override fun onBackPressed() {
        when (binding.bottomNavigationView.selectedItemId){
            R.id.learn ->{
                if (isBackPressedOnce) {
                    Log.e("ObBackPressed_TAG", "The user pressed Back now we exit! ")
                    super.onBackPressed()
                    // Optional: finish() is usually handled by super.onBackPressed() in this context
                    return // Exit early
                }


                isBackPressedOnce = true
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

                // 2. Schedule the reset immediately
                handler.postDelayed(resetBackPressed, 2000) // 2000 ms (2 seconds)
            }
            else -> {
                replaceFragment(StudyLessonsListFragment())
            }
        }
    }


    private fun replaceFragment(frag: Fragment) {
        Log.i("MainActivityTAG", "replaceFragment() called with: frag = $frag")
        var bundle: Bundle = Bundle()
        bundle.putSerializable("userData", userData)
        frag.arguments = bundle
        val FragManager = supportFragmentManager;
        val FragTransition = FragManager.beginTransaction()
        FragTransition.replace(R.id.fr_Container, frag)
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