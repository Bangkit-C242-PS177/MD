package com.example.urkins.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urkins.R
import com.example.urkins.data.adapter.ArticlesAdapter
import com.example.urkins.data.adapter.SkincareRecommendationHomeAdapter
import com.example.urkins.data.response.ArticlesResponse
import com.example.urkins.data.response.SkincareRecommendation
import com.example.urkins.databinding.FragmentHomeBinding
import com.example.urkins.ui.activity.recommendskincare.SkincareRecommendationActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val skincareList = ArrayList<SkincareRecommendation>()
    private val articlesList = ArrayList<ArticlesResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        skincareList.addAll(getListSkincare())
        showSkincareRecyclerList()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvBannerHomeText = view.findViewById<TextView>(R.id.tv_banner_home_text)
        tvBannerHomeText.setOnClickListener {
            val intent = Intent(context, SkincareRecommendationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvArticle.setHasFixedSize(true)
        articlesList.addAll(getListArticles())
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        val articlesAdapter = ArticlesAdapter(articlesList)
        binding.rvArticle.adapter = articlesAdapter
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("Recycle")
    private fun getListArticles(): ArrayList<ArticlesResponse> {
        val dataTitle = resources.getStringArray(R.array.data_article_title)
        val dataDate = resources.getStringArray(R.array.data_article_date)
        val dataAuthor = resources.getStringArray(R.array.data_article_author)
        val dataPhoto = resources.obtainTypedArray(R.array.data_article_photo)
        val listArticles = ArrayList<ArticlesResponse>()
        for (i in dataTitle.indices) {
            val article = ArticlesResponse(dataTitle[i], dataDate[i], dataAuthor[i], dataPhoto.getString(i))
            listArticles.add(article)
        }
        return listArticles
    }

    @SuppressLint("Recycle")
    private fun getListSkincare(): Collection<SkincareRecommendation> {
        val dataSkincareName = resources.getStringArray(R.array.data_skincare_name)
        val dataSkincareCategory = resources.getStringArray(R.array.data_skincare_category)
        val dataSkincarePhoto = resources.obtainTypedArray(R.array.data_skincare_photo)
        val dataSkincareDescription = resources.getStringArray(R.array.data_skincare_description)
        val dataSkincareHowToUse = resources.getStringArray(R.array.data_skincare_how_to_use)
        val dataSkincareIngredients = resources.getStringArray(R.array.data_skincare_ingredients)
        val listRecommendation = ArrayList<SkincareRecommendation>()

        for (i in 0 until minOf(4, dataSkincareName.size)) {
            val skincareRecommendation = SkincareRecommendation(
                dataSkincareName[i], dataSkincareCategory[i], dataSkincareDescription[i], dataSkincareHowToUse[i], dataSkincareIngredients[i], dataSkincarePhoto.getString(i))
            listRecommendation.add(skincareRecommendation)
        }
        return listRecommendation
    }

    private fun showSkincareRecyclerList() {
        binding.rvSkincare.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val skincareRecommendationAdapter = SkincareRecommendationHomeAdapter(skincareList)
        binding.rvSkincare.adapter = skincareRecommendationAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}