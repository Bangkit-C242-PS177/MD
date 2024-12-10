package com.example.urkins.ui.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.MainActivity
import com.example.urkins.R
import com.example.urkins.data.pref.UserPreference
import com.example.urkins.data.pref.dataStore
import com.example.urkins.databinding.ActivitySplashScreenBinding
import com.example.urkins.ui.activity.onboarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val userPreferences = UserPreference.getInstance(application.dataStore)
        splashViewModel = ViewModelProvider(this, SplashViewModelFactory(userPreferences))[SplashViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.isUserTokenAvailable.observe(this) { userTokenAvailable ->
                if (userTokenAvailable) {
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    intent = Intent(this, OnBoardingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 3000)
    }
}