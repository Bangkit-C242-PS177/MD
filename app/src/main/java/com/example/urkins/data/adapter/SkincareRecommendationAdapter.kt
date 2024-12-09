package com.example.urkins.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.urkins.data.response.SkincareRecommendation
import com.example.urkins.databinding.ItemRecommendSkincareBinding

class SkincareRecommendationAdapter(
    private val recommendations: List<SkincareRecommendation>
) : RecyclerView.Adapter<SkincareRecommendationAdapter.SkincareRecommendationViewHolder>() {

    class SkincareRecommendationViewHolder(private val binding: ItemRecommendSkincareBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SkincareRecommendation) {
            Glide.with(binding.root.context)
                .load(item.skincarePhoto)
                .into(binding.ivSkincareProductImg)
            binding.tvSkincareProductName.text = item.skincareName
            binding.tvProductCategory.text = item.skincareCategory
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkincareRecommendationViewHolder {
        val binding = ItemRecommendSkincareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SkincareRecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SkincareRecommendationViewHolder, position: Int) {
        holder.bind(recommendations[position])
    }

    override fun getItemCount(): Int = recommendations.size
}