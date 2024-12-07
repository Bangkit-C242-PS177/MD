package com.example.urkins.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urkins.R
import com.example.urkins.data.adapter.ArticlesAdapter
import com.example.urkins.data.response.ArticlesResponse
import com.example.urkins.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<ArticlesResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        return root
    }

    private fun setupRecyclerView() {
        binding.rvArticle.setHasFixedSize(true)
        list.addAll(getListArticles())
        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        val articlesAdapter = ArticlesAdapter(list)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}