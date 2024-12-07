package com.example.urkins.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.urkins.data.response.ArticlesResponse
import com.example.urkins.databinding.ItemArticlesBinding

class ArticlesAdapter(
    private val articles: ArrayList<ArticlesResponse>
) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(private val binding: ItemArticlesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesResponse) {
            Glide.with(binding.root.context)
                .load(article.articlePhoto)
                .into(binding.ivArticle)
            binding.tvTitleArticles.text = article.articleTitle
            binding.tvPublishedAt.text = article.articleDate
            binding.tvAuthor.text = article.articleAuthor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }
}