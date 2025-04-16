package com.example.centauri

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.centauri.databinding.ActivityMainBinding
import com.example.centauri.fragments.main_nav_frag.StudyLessonsListFragment
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.DbViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

//    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var  authViewModel: AuthViewModel
    private lateinit var  db: DbViewModel
    private lateinit var  dialogWindows: DialogWindows

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

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.main, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        binding.main.addDrawerListener(toggle)
//        toggle.syncState() // I comment this because it appears on the left side

        if (savedInstanceState == null) {
            replaceFragment(StudyLessonsListFragment())
        }


        binding.hamburgerButton.setOnClickListener{
            if (binding.main.isDrawerOpen(GravityCompat.END)) {
             binding.main.closeDrawer(GravityCompat.END)
            } else {
                binding.main.openDrawer(GravityCompat.END)
            }
        }

        if (authViewModel.authState.value == AuthState.Authenticated){
            db.getUserData(authViewModel.getCurrentUser()?.email.toString()){ user->
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.username_text).text = user.username
            }
        }


//        These are for making icons colored
        binding.bottomNavigationView.itemIconTintList = null
        binding.navView.itemIconTintList = null


        binding.bottomNavigationView.selectedItemId = R.id.learn

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.learn -> replaceFragment(StudyLessonsListFragment())
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
                        dialogWindows.showSpaceDialog(R.string.you_are_not_authenticated.toString(), R.string.have_to_create_account.toString(), object : DialogWindows.DialogCallback{
                            override fun onOkCLicked() {
                                var intent = Intent(this@MainActivity, AuthActivity::class.java)
                                startActivity(intent)
                            }
                        })

                    }
                    true
                }
            }

            binding.main.closeDrawer(GravityCompat.END)
            true
        }


        hide_statusbar()







    }


    private fun replaceFragment(frag: Fragment) {
        Log.i("MainActivityTAG", "replaceFragment() called with: frag = $frag")
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