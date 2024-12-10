package com.example.urkins.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.urkins.R
import com.example.urkins.databinding.ActivitySkincareDetailBinding

class SkincareDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySkincareDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkincareDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
       detail()
    }

    private fun detail() {
        binding.progressBar.visibility = View.GONE
        val skincareName = intent.getStringExtra("SKINCARE_NAME")
        val skincareCategory = intent.getStringExtra("SKINCARE_CATEGORY")
        val skincareDescription = intent.getStringExtra("SKINCARE_DESCRIPTION")
        val skincareHowToUse = intent.getStringExtra("SKINCARE_HOW_TO_USE")
        val skincareIngredients = intent.getStringExtra("SKINCARE_INGREDIENTS")
        val skincarePhoto = intent.getStringExtra("SKINCARE_PHOTO")

        Glide.with(this).load(skincarePhoto).into(binding.ivSkincareProductImg)
        binding.tvProductDescription.text = skincareDescription
        binding.tvTitleProductCategory.text = skincareCategory
        binding.tvHowToUseText.text = skincareHowToUse
        binding.tvProductIngredientText.text = skincareIngredients
    }
}