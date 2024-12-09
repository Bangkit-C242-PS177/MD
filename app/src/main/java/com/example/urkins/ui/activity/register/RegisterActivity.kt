package com.example.urkins.ui.activity.register

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
import com.example.urkins.R
import com.example.urkins.databinding.ActivityRegisterBinding
import com.example.urkins.ui.activity.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factoryResult: RegisterViewModelFactory = RegisterViewModelFactory.getInstance(application)
        registerViewModel = ViewModelProvider(this, factoryResult)[RegisterViewModel::class.java]

        registerViewModel.showSuccessDialog.observe(this) {
            showDialog(it)
        }

        registerViewModel.showErrorDialog.observe(this) {
            showErrorDialog(it)
        }

        registerViewModel.loading.observe(this) { isLoading ->
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
        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setUpAction() {
        binding.btnLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val userName = binding.edRegisterName.text.toString().trim()
        val userEmail = binding.edRegisterEmail.text.toString().trim()
        val userPassword = binding.edRegisterPassword.text.toString().trim()
        val userConfirmPassword = binding.edRegisterConfirmPassword.text.toString().trim()

        if (userPassword != userConfirmPassword) {
            showErrorDialog("Password tidak cocok")
            return
        }

        registerViewModel.registerUser(userName, userEmail, userPassword, userConfirmPassword)
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this).setMessage(message)
            .setPositiveButton(R.string.confirmation) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()

        val messageView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageView?.setTextColor(resources.getColor(R.color.black))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    private fun showErrorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this).setMessage(errorMessage)
            .setPositiveButton(R.string.confirmationno) { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
        val messageView = alertDialog.findViewById<TextView>(android.R.id.message)
        messageView?.setTextColor(resources.getColor(R.color.black))
        alertDialog.window?.setBackgroundDrawableResource(R.color.white)
    }

    private fun setMyButtonEnable() {
        val registerNameText = binding.edRegisterName.text
        val registerEmailText = binding.edRegisterEmail.text
        val registerPasswordText = binding.edRegisterPassword.text
        val registerConfirmPasswordText = binding.edRegisterConfirmPassword.text
        binding.btnRegister.isEnabled =
            (registerEmailText.toString().isNotEmpty() && registerPasswordText.toString().isNotEmpty() &&
                    registerNameText.toString().isNotEmpty() && registerConfirmPasswordText.toString().isNotEmpty())
    }
}