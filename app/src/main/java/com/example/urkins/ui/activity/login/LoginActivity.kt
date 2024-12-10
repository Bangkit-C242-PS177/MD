package com.example.urkins.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.MainActivity
import com.example.urkins.R
import com.example.urkins.data.pref.UserPreference
import com.example.urkins.data.pref.dataStore
import com.example.urkins.databinding.ActivityLoginBinding
import com.example.urkins.ui.activity.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userPref = UserPreference.getInstance(application.dataStore)
        val factoryResult: LoginViewModelFactory = LoginViewModelFactory.getInstance(application, userPref)
        loginViewModel = ViewModelProvider(this, factoryResult)[LoginViewModel::class.java]

        loginViewModel.showSuccessDialog.observe(this) {
            showDialog(it)
        }

        loginViewModel.showErrorDialog.observe(this) {
            showErrorDialog(it)
        }

        loginViewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressIndicator.visibility = View.VISIBLE
                binding.overlay.visibility = View.VISIBLE
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.progressIndicator.visibility = View.GONE
                binding.overlay.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }

        setMyButtonEnable()
        setUpAction()
        checkChanged()
    }

    private fun checkChanged() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setUpAction() {
        binding.btnRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog("Email dan Password tidak boleh kosong")
            return
        }

        loginViewModel.loginUser(email, password)
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.confirmation)) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        val messageView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageView?.setTextColor(resources.getColor(R.color.black))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    private fun showErrorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setPositiveButton(getString(R.string.confirmation)) { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
        val messageView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageView?.setTextColor(resources.getColor(R.color.black))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    private fun setMyButtonEnable() {
        val loginEmailText = binding.edLoginEmail.text
        val loginPasswordText = binding.edLoginPassword.text
        binding.btnLogin.isEnabled = (loginEmailText.toString().isNotEmpty() && loginPasswordText.toString().isNotEmpty())
    }
}