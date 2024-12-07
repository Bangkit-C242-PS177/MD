package com.example.urkins.ui.activity.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.urkins.R
import com.example.urkins.data.worker.NotificationWorker
import com.example.urkins.databinding.ActivitySettingBinding
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private var isNotificationEnabled: Boolean? = null
    private var isStatusInitialized = false

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

        supportActionBar?.hide()

        binding.btnNotificationArrow.setOnClickListener {
            if (isNotificationEnabled == true) {
                showDisableNotificationDialog()
            } else {
                checkAndRequestNotificationPermission()
            }
        }

        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        updateNotificationStatus()
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
}