package com.example.urkins.ui.activity.result

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.urkins.R
import com.example.urkins.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val resultViewModel: ResultViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil data dari Intent
        val imageUri: Uri? = intent.getParcelableExtra(EXTRA_IMAGE)
        val skinConditions = intent.getSerializableExtra("skin_conditions") as ArrayList<List<String>>
        val skinType = intent.getSerializableExtra("skin_type") as ArrayList<List<String>>

        binding.ivResultImage.setImageURI(imageUri)
        // Masukkan data ke ViewModel
        resultViewModel.setData(skinConditions, skinType)

        // Observasi data untuk menampilkan hasil
        observeViewModel()
    }

    private fun observeViewModel() {
//        val ivResultImage = findViewById<ImageView>(R.id.iv_result_image)
        val tvSkinConditions = findViewById<TextView>(R.id.result_skin_condition_text)
        val tvSkinType = findViewById<TextView>(R.id.result_skin_type_text)

        resultViewModel.skinConditions.observe(this, Observer { conditions ->
            tvSkinConditions.text = conditions.joinToString("\n") { "${it[0]}: ${it[1]}" }
        })

        resultViewModel.skinType.observe(this, Observer { types ->
            tvSkinType.text = types.joinToString("\n") { "${it[0]}: ${it[1]}" }
        })
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"

    }
}