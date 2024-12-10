package com.example.urkins.ui.activity.recommendskincare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.urkins.MainActivity
import com.example.urkins.R
import com.example.urkins.data.adapter.SkincareRecommendationAdapter
import com.example.urkins.data.response.SkincareRecommendation
import com.example.urkins.databinding.ActivitySkincareRecommendationBinding
import com.example.urkins.ui.activity.SkincareDetailActivity

class SkincareRecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySkincareRecommendationBinding
    private val list = ArrayList<SkincareRecommendation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkincareRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.rvSkincare.setHasFixedSize(true)
        list.addAll(getListSkincare())
        showSkincareRecyclerList()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListSkincare(): ArrayList<SkincareRecommendation> {
        val dataSkincareName = resources.getStringArray(R.array.data_skincare_name)
        val dataSkincareCategory = resources.getStringArray(R.array.data_skincare_category)
        val dataSkincarePhoto = resources.obtainTypedArray(R.array.data_skincare_photo)
        val dataSkincareDescription = resources.getStringArray(R.array.data_skincare_description)
        val dataSkincareHowToUse = resources.getStringArray(R.array.data_skincare_how_to_use)
        val dataSkincareIngredients = resources.getStringArray(R.array.data_skincare_ingredients)
        val listRecommendation = ArrayList<SkincareRecommendation>()

        for (i in dataSkincareName.indices) {
            val skincareRecommendation = SkincareRecommendation(
                dataSkincareName[i], dataSkincareCategory[i], dataSkincareDescription[i], dataSkincareHowToUse[i], dataSkincareIngredients[i], dataSkincarePhoto.getString(i)
            )
            listRecommendation.add(skincareRecommendation)
        }
        dataSkincarePhoto.recycle()
        return listRecommendation
    }

    private fun showSkincareRecyclerList() {
        binding.progressBar.visibility = View.GONE
        binding.rvSkincare.layoutManager = GridLayoutManager(this, 2)
        val listRecommendationAdapter = SkincareRecommendationAdapter(list) { skincareRecommendation ->
            val intent = Intent(this, SkincareDetailActivity::class.java).apply {
                putExtra("SKINCARE_NAME", skincareRecommendation.skincareName)
                putExtra("SKINCARE_CATEGORY", skincareRecommendation.skincareCategory)
                putExtra("SKINCARE_DESCRIPTION", skincareRecommendation.skincareDescription)
                putExtra("SKINCARE_HOW_TO_USE", skincareRecommendation.skincareHowToUse)
                putExtra("SKINCARE_INGREDIENTS", skincareRecommendation.skincareIngredients)
                putExtra("SKINCARE_PHOTO", skincareRecommendation.skincarePhoto)
            }
            startActivity(intent)
        }
        binding.rvSkincare.adapter = listRecommendationAdapter
    }
}