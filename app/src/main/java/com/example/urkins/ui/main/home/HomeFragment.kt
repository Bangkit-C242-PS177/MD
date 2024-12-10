package com.example.urkins.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urkins.R
import com.example.urkins.data.adapter.ArticlesAdapter
import com.example.urkins.data.adapter.SkincareRecommendationHomeAdapter
import com.example.urkins.data.response.ArticlesResponse
import com.example.urkins.data.response.SkincareRecommendation
import com.example.urkins.databinding.FragmentHomeBinding
import com.example.urkins.ui.activity.SkincareDetailActivity
import com.example.urkins.ui.activity.recommendskincare.SkincareRecommendationActivity
import com.example.urkins.data.pref.UserPreference2
import com.example.urkins.data.pref.UserModel
import com.example.urkins.data.pref.dataStore
import com.example.urkins.data.repository.UserRepository

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val skincareList = ArrayList<SkincareRecommendation>()
    private val articlesList = ArrayList<ArticlesResponse>()

    private lateinit var userPreference: UserPreference2

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userPreference = UserPreference2.getInstance(requireContext().dataStore)
        val userRepository = UserRepository.getInstance(userPreference)
        val factory = HomeViewModelFactory(userRepository)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        skincareList.addAll(getListSkincare())
        observeUserSession()

        return binding.root
    }

    private fun observeUserSession() {
        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                binding.clSkincareRecommendHome.visibility = View.GONE
                binding.clSkincareRecommendHomeGuest.visibility = View.VISIBLE
            } else {
                binding.clSkincareRecommendHome.visibility = View.VISIBLE
                binding.clSkincareRecommendHomeGuest.visibility = View.GONE
                showSkincareRecyclerList()
            }
        }
    }

    private fun showSkincareRecyclerList() {
        binding.rvSkincare.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSkincare.adapter = SkincareRecommendationHomeAdapter(skincareList) { skincareRecommendation ->
            startActivity(Intent(requireContext(), SkincareDetailActivity::class.java).apply {
                putExtra("SKINCARE_NAME", skincareRecommendation.skincareName)
                putExtra("SKINCARE_CATEGORY", skincareRecommendation.skincareCategory)
                putExtra("SKINCARE_DESCRIPTION", skincareRecommendation.skincareDescription)
                putExtra("SKINCARE_HOW_TO_USE", skincareRecommendation.skincareHowToUse)
                putExtra("SKINCARE_INGREDIENTS", skincareRecommendation.skincareIngredients)
                putExtra("SKINCARE_PHOTO", skincareRecommendation.skincarePhoto)
            })
        }
    }

    private fun setupRecyclerView() {
        binding.rvArticle.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ArticlesAdapter(getListArticles())
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun getListArticles(): ArrayList<ArticlesResponse> {
        val dataTitle = resources.getStringArray(R.array.data_article_title)
        val dataDate = resources.getStringArray(R.array.data_article_date)
        val dataAuthor = resources.getStringArray(R.array.data_article_author)
        val dataPhoto = resources.obtainTypedArray(R.array.data_article_photo)
        return ArrayList<ArticlesResponse>().apply {
            for (i in dataTitle.indices) {
                add(ArticlesResponse(dataTitle[i], dataDate[i], dataAuthor[i], dataPhoto.getString(i)))
            }
            dataPhoto.recycle()
        }
    }

    private fun getListSkincare(): Collection<SkincareRecommendation> {
        val dataSkincareName = resources.getStringArray(R.array.data_skincare_name)
        val dataSkincareCategory = resources.getStringArray(R.array.data_skincare_category)
        val dataSkincarePhoto = resources.obtainTypedArray(R.array.data_skincare_photo)
        val dataSkincareDescription = resources.getStringArray(R.array.data_skincare_description)
        val dataSkincareHowToUse = resources.getStringArray(R.array.data_skincare_how_to_use)
        val dataSkincareIngredients = resources.getStringArray(R.array.data_skincare_ingredients)

        return ArrayList<SkincareRecommendation>().apply {
            for (i in 0 until minOf(4, dataSkincareName.size)) {
                add(SkincareRecommendation(
                    dataSkincareName[i], dataSkincareCategory[i], dataSkincareDescription[i],
                    dataSkincareHowToUse[i], dataSkincareIngredients[i], dataSkincarePhoto.getString(i)
                ))
            }
            dataSkincarePhoto.recycle()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBannerHomeText.setOnClickListener {
            startActivity(Intent(context, SkincareRecommendationActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}