package com.example.urkins.ui.activity.setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.urkins.R
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.UserRepository
import com.example.urkins.data.worker.NotificationWorker
import com.example.urkins.databinding.ActivitySettingBinding
import com.example.urkins.ui.activity.login.LoginViewModel
import com.example.urkins.ui.activity.login.LoginViewModelFactory
import com.example.urkins.ui.activity.onboarding.OnBoardingActivity
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class SettingActivity : AppCompatActivity() {
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var binding: ActivitySettingBinding
    private var isNotificationEnabled: Boolean? = null
    private var isStatusInitialized = false
    private val REQUEST_CODE_PICK_IMAGE = 100

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this,
                    getString(R.string.notifications_permission_granted), Toast.LENGTH_SHORT).show()
                binding.tvNotificationStatus.text = getString(R.string.notification_enabled)
                scheduleNotification()
                isNotificationEnabled = true
                isStatusInitialized = true
            } else {
                Toast.makeText(this,
                    getString(R.string.notifications_permission_rejected), Toast.LENGTH_SHORT).show()
                binding.tvNotificationStatus.text = getString(R.string.notification_disabled)
                isNotificationEnabled = false
                isStatusInitialized = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPref = UserPreference2.getInstance(application.dataStore)
        val userRepo = UserRepository.getInstance(userPref)
        val factoryResult: SettingViewModelFactory = SettingViewModelFactory.getInstance( userPref, userRepo)
        settingViewModel = ViewModelProvider(this, factoryResult)[SettingViewModel::class.java]

        setupView()
        setupAction()

        binding.btnNotificationArrow.setOnClickListener {
            if (isNotificationEnabled == true) {
                showDisableNotificationDialog()
            } else {
                checkAndRequestNotificationPermission()
            }
        }

        binding.btnProfileEditArrow.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        logoutUser()
        updateNotificationStatus()
    }

    private fun setupAction() {
        binding.btnLanguageArrow.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun logoutUser() {
        binding.btnLogout.setOnClickListener {
            dialogConfirmationLogout()
        }
    }

    private fun dialogConfirmationLogout() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.logout_title))
            setMessage(getString(R.string.logout_confirmation))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                settingViewModel.logout()
                val intent = Intent(this@SettingActivity, OnBoardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            setNegativeButton(getString(R.string.no), null)
            create()
            show()
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                scheduleNotification()
                binding.tvNotificationStatus.text = getString(R.string.notification_enabled)
                isNotificationEnabled = true
                isStatusInitialized = true
            }
        }
    }

    private fun scheduleNotification() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(14, TimeUnit.DAYS)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun updateNotificationStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val status = if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                getString(R.string.notification_enabled)
            } else {
                getString(R.string.notification_disabled)
            }
            if (isStatusInitialized) {
                binding.tvNotificationStatus.text = status
            }
            isNotificationEnabled = status == getString(R.string.notification_enabled)
        }
    }

    private fun showDisableNotificationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.notifications_disable_title_dialog))
        builder.setMessage(getString(R.string.notification_disabled_dialog_text))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            binding.tvNotificationStatus.text = getString(R.string.notification_disabled)
            isNotificationEnabled = false
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            val intent = Intent().apply {
                putExtra("image_uri", uri.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}