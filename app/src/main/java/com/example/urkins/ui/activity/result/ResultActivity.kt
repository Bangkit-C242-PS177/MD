package com.example.urkins.ui.activity.result

import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import com.example.urkins.R
import com.example.urkins.databinding.ActivityResultBinding
import com.example.urkins.ui.activity.recommendskincare.SkincareRecommendationActivity
import com.google.android.material.snackbar.Snackbar

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private  lateinit var resultViewModel: ResultViewModel
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

        supportActionBar?.hide()

        val result : ResultViewModelFactory = ResultViewModelFactory.getInstance(application)
        resultViewModel = ViewModelProvider(this, result)[ResultViewModel::class.java]

        resultViewModel.snackBar.observe(this) {
            Snackbar.make(window.decorView.rootView, it, Snackbar.LENGTH_SHORT).show()
        }

        // Ambil data dari Intent
        val imageUri: Uri? = intent.getParcelableExtra(EXTRA_IMAGE)
        val prediction = intent.getStringExtra(EXTRA_RESULT) ?: "Eye Bag"
        val prediction2 = intent.getStringExtra(EXTRA_RESULT2) ?: "Normal"

        // Tampilkan data di UI
        binding.ivResultImage.setImageURI(imageUri)
        binding.resultSkinConditionText.text = prediction
        binding.resultSkinTypeText.text = prediction2

        binding.btnSkincareRecommendation.setOnClickListener {
            if (imageUri != null && prediction != null && prediction2 != null) {
                resultViewModel.saveHistory(imageUri, prediction, prediction2)
            } else {
                Snackbar.make(window.decorView.rootView, "History Not Saved", Snackbar.LENGTH_SHORT).show()
            }
            val intent = Intent(this, SkincareRecommendationActivity::class.java)
            startActivity(intent)
        }

//        val skinConditions = intent.getSerializableExtra("skin_conditions") as ArrayList<List<String>>
//        val skinType = intent.getSerializableExtra("skin_type") as ArrayList<List<String>>

//        binding.ivResultImage.setImageURI(imageUri)
//        resultViewModel.setData(skinConditions, skinType)


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
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_RESULT2 = "extra_result2"

    }
}